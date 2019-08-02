package com.example.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.domain.Consumer;
import com.example.demo.service.impl.ConsumerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@Controller
public class LoginController {

    @Autowired
    private ConsumerServiceImpl consumerService;

//    添加用户
    @ResponseBody
    @RequestMapping(value = "/api/signup", method = RequestMethod.POST)
    public Object signup(HttpServletRequest req){
        JSONObject jsonObject = new JSONObject();
        String username = req.getParameter("username").trim();
        String password = req.getParameter("password").trim();
        String sex = req.getParameter("sex").trim();
        String phone_num = req.getParameter("phone_num").trim();
        String email = req.getParameter("email").trim();
        String birth = req.getParameter("birth").trim();
        String introduction = req.getParameter("introduction").trim();
        String location = req.getParameter("location").trim();
        String avator = req.getParameter("avator").trim();

        if (username.equals("") || username == null){
            jsonObject.put("code", 0);
            jsonObject.put("msg", "用户名或密码错误");
            return jsonObject;
        }
        Consumer consumer = new Consumer();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date myBirth = new Date();
        try {
            myBirth = dateFormat.parse(birth);
        }catch (Exception e){
            e.printStackTrace();
        }
        consumer.setUsername(username);
        consumer.setPassword(password);
        consumer.setSex(new Byte(sex));
        if(phone_num == ""){
            consumer.setPhoneNum(null);
        }else{
            consumer.setPhoneNum(phone_num);
        }

        if(email == ""){
            consumer.setEmail(null);
        }else{
            consumer.setEmail(email);
        }
        consumer.setBirth(myBirth);
        consumer.setIntroduction(introduction);
        consumer.setLocation(location);
        consumer.setAvator(avator);
        consumer.setCreateTime(new Date());
        consumer.setUpdateTime(new Date());

        boolean res = consumerService.addUser(consumer);
        if (res){
            jsonObject.put("code", 1);
            jsonObject.put("msg", "登录成功");
            return jsonObject;
        }else {
            jsonObject.put("code", 0);
            jsonObject.put("msg", "用户名或密码错误");
            return jsonObject;
        }
    }

//    判断是否登录成功
    @ResponseBody
    @RequestMapping(value = "/api/loginVerify", method = RequestMethod.POST)
    public Object loginVerify(HttpServletRequest req, HttpSession session){

        JSONObject jsonObject = new JSONObject();
        String username = req.getParameter("username");
        String password = req.getParameter("password");
//        System.out.println(username+"  "+password);
        boolean res = consumerService.veritypasswd(username, password);

        if (res){
            jsonObject.put("code", 1);
            jsonObject.put("msg", "登录成功");
            jsonObject.put("userMsg", consumerService.consumerLists(username));
            session.setAttribute("username", username);
            return jsonObject;
        }else {
            jsonObject.put("code", 0);
            jsonObject.put("msg", "用户名或密码错误");
            return jsonObject;
        }

    }

//    删除用户
    @RequestMapping(value = "/api/deleteUsers", method = RequestMethod.GET)
    public Object deleteUsers(HttpServletRequest req){
        String id = req.getParameter("id");
        return consumerService.deleteUser(Integer.parseInt(id));
    }

//    更新用户信息
    @ResponseBody
    @RequestMapping(value = "/api/updateUserMsgs", method = RequestMethod.POST)
    public Object updateUserMsgs(HttpServletRequest req){
        JSONObject jsonObject = new JSONObject();
        String id = req.getParameter("id").trim();
        String username = req.getParameter("username").trim();
        String password = req.getParameter("password").trim();
        String sex = req.getParameter("sex").trim();
        String phone_num = req.getParameter("phone_num").trim();
        String email = req.getParameter("email").trim();
        String birth = req.getParameter("birth").trim();
        String introduction = req.getParameter("introduction").trim();
        String location = req.getParameter("location").trim();
//        String avator = req.getParameter("avator").trim();
//        System.out.println(username+"  "+password+"  "+sex+"   "+phone_num+"     "+email+"      "+birth+"       "+introduction+"      "+location);

        if (username.equals("") || username == null){
            jsonObject.put("code", 0);
            jsonObject.put("msg", "用户名或密码错误");
            return jsonObject;
        }
        Consumer consumer = new Consumer();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date myBirth = new Date();
        try {
            myBirth = dateFormat.parse(birth);
        }catch (Exception e){
            e.printStackTrace();
        }
        consumer.setId(Integer.parseInt(id));
        consumer.setUsername(username);
        consumer.setPassword(password);
        consumer.setSex(new Byte(sex));
        consumer.setPhoneNum(phone_num);
        consumer.setEmail(email);
        consumer.setBirth(myBirth);
        consumer.setIntroduction(introduction);
        consumer.setLocation(location);
//        consumer.setAvator(avator);
        consumer.setUpdateTime(new Date());

        boolean res = consumerService.updateUserMsg(consumer);
        if (res){
            jsonObject.put("code", 1);
            jsonObject.put("msg", "修改成功");
            return jsonObject;
        }else {
            jsonObject.put("code", 0);
            jsonObject.put("msg", "修改失败");
            return jsonObject;
        }
    }

//    更新用户头像
    @ResponseBody
    @RequestMapping(value = "/api/updateUserImg", method = RequestMethod.POST)
    public Object updateUserImg(@RequestParam("file") MultipartFile avatorFile, @RequestParam("id")int id){
        JSONObject jsonObject = new JSONObject();

        if (avatorFile.isEmpty()) {
            jsonObject.put("code", 0);
            jsonObject.put("msg", "文件上传失败！");
            return jsonObject;
        }
        String fileName = System.currentTimeMillis()+avatorFile.getOriginalFilename();
        String filePath = System.getProperty("user.dir") + System.getProperty("file.separator") + "avatorImages" ;
        File file1 = new File(filePath);
        if (!file1.exists()){
            file1.mkdir();
        }

        File dest = new File(filePath + System.getProperty("file.separator") + fileName);
        String storeAvatorPath = "/avatorImages/"+fileName;
        try {
            avatorFile.transferTo(dest);
            Consumer consumer = new Consumer();
            consumer.setId(id);
            consumer.setAvator(storeAvatorPath);
            boolean res = consumerService.updateUserAvator(consumer);
            if (res){
                jsonObject.put("code", 1);
                jsonObject.put("avator", storeAvatorPath);
                jsonObject.put("msg", "上传成功");
                return jsonObject;
            }else {
                jsonObject.put("code", 0);
                jsonObject.put("msg", "上传失败");
                return jsonObject;
            }
        }catch (IOException e){
            jsonObject.put("code", 0);
            jsonObject.put("msg", "上传失败"+e.getMessage());
            return jsonObject;
        }finally {
            return jsonObject;
        }
    }

    @Configuration
    public class MyPicConfig implements WebMvcConfigurer {
        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
            registry.addResourceHandler("/avatorImages/**").addResourceLocations("file:/Users/yhw/Documents/github-workspace/music-website/music-server/avatorImages/");
        }
    }

//    返回指定ID的用户
    @RequestMapping(value = "/commentOfConsumer", method = RequestMethod.GET)
    public Object toSongs(HttpServletRequest req){
        String id = req.getParameter("id");
        return consumerService.conmmentUser(Integer.parseInt(id));
    }

//    返回所有用户
    @RequestMapping(value = "/AllUsers", method = RequestMethod.GET)
    public Object AllUsers(){
        return consumerService.allUser();
    }
}
