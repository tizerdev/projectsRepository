package top.tizer.socketeasy.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ServerEndpoint("/websocket/{username}")
public class WebSocket {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 在线人数
     */
    public static int onlineNumber = 0;

    /**
     * 以用户姓名为key，WebSocket为对象保存起来
     */
    private static Map<String,WebSocket> clients = new ConcurrentHashMap<String,WebSocket>();

    /**
     * 会话
     */
    private Session session;
    /**
     * 用户名称
     */
    private String username;

    /**
     * 建立连接
     * @param username
     * @param session
     */
    @OnOpen
    public void onOpen(@PathParam("username") String username,Session session){
        logger.info("现在连接的客户id为："+session.getId()+",用户名："+username);
        this.username = username;
        this.session = session;
        clients.put(username,this);
        addOnlineCount();
        logger.info("当前连接人数为："+getOnlineCount());
        try {
            sendMessageTo("连接已成功建立",username);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @OnError
    public void onError(Session session,Throwable error){
        logger.error("服务端发生了 错误："+error.getMessage());
    }

    /**
     * 连接关闭
     */
    @OnClose
    public void onClose(){
        clients.remove(username);
        subOnlineCount();
        //通知全部用户
        try {
            sendMessageAll("用户"+username+"下线了","system");
        }catch (IOException e){
            e.printStackTrace();
        }
        logger.info("当前连接人数为："+getOnlineCount());
    }

    /**
     * 收到客户端的消息  发送
     * @param message   消息
     * @param session   会话
     */
    @OnMessage
    public void onMessage(String message,Session session){
        try {
            logger.info("onMessage:"+message);
            JSONObject jsonObject = JSON.parseObject(message);
            logger.info(jsonObject.toString());
            String username = jsonObject.getString("username");
            logger.info("username:"+username);
            String textMessage = jsonObject.getString("message");
            logger.info("textMessage:"+textMessage);
            String receiver = jsonObject.getString("receiver");
            logger.info("receiver:"+receiver);
            if (receiver.equals("all")){
                //发送给全部用户不包括自己
                sendMessageAll(textMessage,username);
            }else {
                //无法给自己发送消息
                if (username.equals(receiver)){
                    sendMessageTo("system:无法给自己发送消息",username);
                    return;
                }
                //判断用户是否在线
                if(userOnLine(receiver)){
                    sendMessageTo(username+":"+textMessage,receiver);
                }else {
                    //发送给返回方,用户不在线
                    sendMessageTo("system:用户"+receiver+"不在线",username);
                }
            }
        }catch (Exception e){
            logger.info("发生了错误！");
            e.printStackTrace();
        }
    }

    //判断用户是否在线
    public boolean userOnLine(String username){
        Set<String> keys = clients.keySet();
        if (keys.contains(username)){
            return true;
        }
        return false;
    }

    public void sendMessageTo(String message,String toUsername) throws IOException{
        for (WebSocket item : clients.values()){
            if (item.username.equals(toUsername)){
                item.session.getAsyncRemote().sendText(message);
                break;
            }
        }
    }
    public void sendMessageAll(String message,String FromUsername) throws IOException{
        for (WebSocket item:clients.values()){
            if (!item.username.equals(FromUsername)){
                item.session.getAsyncRemote().sendText(FromUsername+":"+message);
            }
        }
    }

    //连接人数+1
    public static synchronized void addOnlineCount(){
        onlineNumber ++;
    }

    //连接人数-1
    public static synchronized void subOnlineCount(){
        onlineNumber --;
    }

    //获取当前在线人数
    public static synchronized int getOnlineCount(){
        return onlineNumber;
    }


}
