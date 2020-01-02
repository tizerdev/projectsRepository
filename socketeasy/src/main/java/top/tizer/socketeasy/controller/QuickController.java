package top.tizer.socketeasy.controller;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import top.tizer.socketeasy.entity.Message;

@RestController
@Slf4j
public class QuickController {
    @Autowired
    private WebSocket webSocket;

    @RequestMapping("/sendMsg")
    public String sendMsg(String msg) throws Exception{
        Message message = new Message();
        message.setMessage(msg);
        message.setTo("All");
        String s = JSON.toJSONString(message);
        log.info(s);
        //webSocket.onMessage(s,null);
        webSocket.sendMessageAll(s,"Admin");
        return "SUCCESS";
    }
}
