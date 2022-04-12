package com.example.yin.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.yin.common.ErrorMessage;
import com.example.yin.common.FailMessage;
import com.example.yin.common.SuccessMessage;
import com.example.yin.constant.Constants;
import com.example.yin.domain.Consumer;
import com.example.yin.service.impl.ConsumerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
public class ConsumerController {

    @Autowired
    private ConsumerServiceImpl consumerService;

    @Configuration
    public static class MyPicConfig implements WebMvcConfigurer {
        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
            registry.addResourceHandler("/img/avatorImages/**")
                    .addResourceLocations(Constants.AVATOR_IMAGES_PATH);
        }
    }

    /**
     * 用户注册
     */
    @ResponseBody
    @RequestMapping(value = "/user/add", method = RequestMethod.POST)
    public Object addUser(HttpServletRequest req) {
        String username = req.getParameter("username").trim();
        String password = req.getParameter("password").trim();
        String sex = req.getParameter("sex").trim();
        String phone_num = req.getParameter("phone_num").trim();
        String email = req.getParameter("email").trim();
        String birth = req.getParameter("birth").trim();
        String introduction = req.getParameter("introduction").trim();
        String location = req.getParameter("location").trim();
        String avator = "/img/avatorImages/user.jpg";

        Consumer consumer = new Consumer();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date myBirth = new Date();
        try {
            myBirth = dateFormat.parse(birth);
        } catch (Exception e) {
            e.printStackTrace();
        }
        consumer.setUsername(username);
        consumer.setPassword(password);
        consumer.setSex(new Byte(sex));
        if ("".equals(phone_num)) {
            consumer.setPhoneNum(null);
        } else {
            consumer.setPhoneNum(phone_num);
        }

        if ("".equals(email)) {
            consumer.setEmail(null);
        } else {
            consumer.setEmail(email);
        }
        consumer.setBirth(myBirth);
        consumer.setIntroduction(introduction);
        consumer.setLocation(location);
        consumer.setAvator(avator);
        consumer.setCreateTime(new Date());
        consumer.setUpdateTime(new Date());

        try {
            boolean res = consumerService.addUser(consumer);
            if (res) {
                return new SuccessMessage("注册成功").getMessage();
            } else {
                return new FailMessage("注册失败").getMessage();
            }
        } catch (DuplicateKeyException e) {
            return new ErrorMessage("用户名已注册").getMessage();
        }
    }

    /**
     * 登录判断
     */
    @ResponseBody
    @RequestMapping(value = "/user/login/status", method = RequestMethod.POST)
    public Object loginStatus(HttpServletRequest req, HttpSession session) {
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        boolean res = consumerService.veritypasswd(username, password);

        if (res) {
            JSONObject jsonObject = new SuccessMessage("登录成功").getMessage();
            jsonObject.put("data", consumerService.loginStatus(username));
            session.setAttribute("username", username);
            return jsonObject;
        } else {
            return new FailMessage("用户名或密码错误").getMessage();
        }
    }

    /**
     * 返回所有用户
     */
    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public Object allUser() {
        return consumerService.allUser();
    }

    /**
     * 返回指定ID的用户
     */
    @RequestMapping(value = "/user/detail", method = RequestMethod.GET)
    public Object userOfId(HttpServletRequest req) {
        String id = req.getParameter("id");
        return consumerService.userOfId(Integer.parseInt(id));
    }

    /**
     * 删除用户
     */
    @RequestMapping(value = "/user/delete", method = RequestMethod.GET)
    public Object deleteUser(HttpServletRequest req) {
        String id = req.getParameter("id");
        boolean res = consumerService.deleteUser(Integer.parseInt(id));
        if (res) {
            return new SuccessMessage("删除成功").getMessage();
        } else {
            return new FailMessage("删除失败").getMessage();
        }
    }

    /**
     * 更新用户信息
     */
    @ResponseBody
    @RequestMapping(value = "/user/update", method = RequestMethod.POST)
    public Object updateUserMsg(HttpServletRequest req) {
        String id = req.getParameter("id").trim();
        String username = req.getParameter("username").trim();
        String password = req.getParameter("password").trim();
        String sex = req.getParameter("sex").trim();
        String phone_num = req.getParameter("phone_num").trim();
        String email = req.getParameter("email").trim();
        String birth = req.getParameter("birth").trim();
        String introduction = req.getParameter("introduction").trim();
        String location = req.getParameter("location").trim();
        // System.out.println(username+" "+password);

        Consumer consumer = new Consumer();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date myBirth = new Date();
        try {
            myBirth = dateFormat.parse(birth);
        } catch (Exception e) {
            e.printStackTrace();
        }
        consumer.setId(Integer.parseInt(id));
        consumer.setUsername(username);
        consumer.setPassword(password);
        consumer.setSex(new Byte(sex));
        consumer.setPhoneNum(phone_num);
        consumer.setEmail(email);
        consumer.setIntroduction(introduction);
        consumer.setLocation(location);
        consumer.setUpdateTime(new Date());
        consumer.setBirth(myBirth);

        boolean res = consumerService.updateUserMsg(consumer);
        if (res) {
            return new SuccessMessage("修改成功").getMessage();
        } else {
            return new FailMessage("修改失败").getMessage();
        }
    }

    /**
     * 更新用户头像
     */
    @ResponseBody
    @RequestMapping(value = "/user/avatar/update", method = RequestMethod.POST)
    public Object updateUserPic(@RequestParam("file") MultipartFile avatorFile, @RequestParam("id") int id) {
        String fileName = System.currentTimeMillis() + avatorFile.getOriginalFilename();
        String filePath = Constants.PROJECT_PATH + System.getProperty("file.separator") + "img"
                + System.getProperty("file.separator") + "avatorImages";
        File file1 = new File(filePath);
        if (!file1.exists()) {
            file1.mkdir();
        }

        File dest = new File(filePath + System.getProperty("file.separator") + fileName);
        String storeAvatorPath = "/img/avatorImages/" + fileName;
        try {
            avatorFile.transferTo(dest);
            Consumer consumer = new Consumer();
            consumer.setId(id);
            consumer.setAvator(storeAvatorPath);
            boolean res = consumerService.updateUserAvator(consumer);
            if (res) {
                JSONObject jsonObject = new SuccessMessage("上传成功").getMessage();
                jsonObject.put("data", storeAvatorPath);
                return jsonObject;
            } else {
                return new FailMessage("上传失败").getMessage();
            }
        } catch (IOException e) {
            return new ErrorMessage("上传失败" + e.getMessage()).getMessage();
        }
    }
}
