package top.tizer.doubledatasource.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import top.tizer.doubledatasource.entity.UserInfo;
import top.tizer.doubledatasource.mapper.oracle.UserInfoMapper;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class UserInfoController {

    @Autowired
    private UserInfoMapper userInfoMapper;

    @RequestMapping(value = "all",method = RequestMethod.POST)
    public String getAllUserInfo(HttpServletRequest request){
        List<UserInfo> userInfos = userInfoMapper.selectAll();
        JSONObject obj = new JSONObject();
        obj.put("code",200);
        obj.put("msg","SUCCESS");
        JSONArray datas = new JSONArray();
        for (UserInfo userinfo :
                userInfos) {
            datas.add(userinfo);
        }
        obj.put("data",datas);
        return obj.toString();
    }
}
