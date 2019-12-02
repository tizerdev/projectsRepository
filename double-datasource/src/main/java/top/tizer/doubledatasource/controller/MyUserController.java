package top.tizer.doubledatasource.controller;


import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.tizer.doubledatasource.entity.MyUser;
import top.tizer.doubledatasource.mapper.mysql.MyUserMapper;

import javax.servlet.http.HttpServletRequest;

@RestController
public class MyUserController {
    @Autowired
    private MyUserMapper myUserMapper;

    @RequestMapping(value = "/alluser",method = RequestMethod.POST)
    public String getAllMyUser(HttpServletRequest request, @RequestParam(value = "uid",required = true) Integer uid){
        System.out.println("alluer--"+uid);
        MyUser myUser = myUserMapper.selectByPrimaryKey(uid);
        JSONObject obj = new JSONObject();
        obj.put("id",myUser.getId());
        obj.put("name",myUser.getName());
        obj.put("password",myUser.getPassword());
        return obj.toString();
    }
}
