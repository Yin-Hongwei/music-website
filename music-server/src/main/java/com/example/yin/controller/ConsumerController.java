package com.example.yin.controller;

import com.example.yin.common.Message;
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

    private Message message = new Message();

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

        if (consumerService.existUser(username)) {
            return message.warning("用户名已注册");
        }

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
                return message.success("注册成功");
            } else {
                return message.error("注册失败");
            }
        } catch (DuplicateKeyException e) {
            return message.fatal(e.getMessage());
        }
    }

    /**
     * 登录判断
     * 这里用不了  requestBody 因为前端传回来的东西 不是j
     */
    @PostMapping("/user/login/status")
    public Object loginStatus(HttpServletRequest req, HttpSession session) {
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        boolean res = consumerService.verityPasswd(username, password);

        //TODO 登陆这块的话  暂时可以不用修改
        if (res) {
            session.setAttribute("username", username);
            return message.success("登录成功", consumerService.loginStatus(username));
        } else {
            return message.error("用户名或密码错误");
        }
    }

    /**
     * 返回所有用户
     */
    @GetMapping("/user")
    public Object allUser() {
        return message.success(null, consumerService.allUser());
    }

    /**
     * 返回指定 ID 的用户
     */
    @GetMapping("/user/detail")
    public Object userOfId(HttpServletRequest req) {
        String id = req.getParameter("id");

        return message.success(null, consumerService.userOfId(Integer.parseInt(id)));
    }

    /**
     * 删除用户
     */
    @GetMapping("/user/delete")
    public Object deleteUser(HttpServletRequest req) {
        String id = req.getParameter("id");

        boolean res = consumerService.deleteUser(Integer.parseInt(id));
        if (res) {
            return message.success("删除成功");
        } else {
            return message.error("删除失败");
        }
    }

    /**
     * 更新用户信息
     */
    @PostMapping("/user/update")
    public Object updateUserMsg(HttpServletRequest req) {
        String id = req.getParameter("id").trim();
        String username = req.getParameter("username").trim();
        String sex = req.getParameter("sex").trim();
        String phoneNum = req.getParameter("phone_num").trim();
        String email = req.getParameter("email").trim();
        String birth = req.getParameter("birth").trim();
        String introduction = req.getParameter("introduction").trim();
        String location = req.getParameter("location").trim();
        // System.out.println(username);

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
        consumer.setSex(new Byte(sex));
        consumer.setPhoneNum(phoneNum);
        consumer.setEmail(email);
        consumer.setIntroduction(introduction);
        consumer.setLocation(location);
        consumer.setUpdateTime(new Date());
        consumer.setBirth(myBirth);

        boolean res = consumerService.updateUserMsg(consumer);
        if (res) {
            return message.success("修改成功");
        } else {
            return message.error("修改失败");
        }
    }

    /**
     * 更新用户密码
     */
    @PostMapping("/user/updatePassword")
    public Object updatePassword(HttpServletRequest req) {
        String id = req.getParameter("id").trim();
        String username = req.getParameter("username").trim();
        String old_password = req.getParameter("old_password").trim();
        String password = req.getParameter("password").trim();

        boolean res = consumerService.verityPasswd(username, old_password);
        if (!res) {
            return message.error("密码输入错误");
        }

        Consumer consumer = new Consumer();
        consumer.setId(Integer.parseInt(id));
        consumer.setPassword(password);

        boolean result = consumerService.updatePassword(consumer);
        if (result) {
            return message.success("密码修改成功");
        } else {
            return message.error("密码修改失败");
        }
    }

    /**
     * 更新用户头像
     */
    @ResponseBody
    @PostMapping("/user/avatar/update")
    public Object updateUserPic(@RequestParam("file") MultipartFile avatorFile, @RequestParam("id") int id) {
        String fileName = System.currentTimeMillis() + avatorFile.getOriginalFilename();
        String filePath = Constants.PROJECT_PATH + System.getProperty("file.separator") + "img" + System.getProperty("file.separator") + "avatorImages";
        File file1 = new File(filePath);
        if (!file1.exists()) {
            file1.mkdir();
        }

        File dest = new File(filePath + System.getProperty("file.separator") + fileName);
        String imgPath = "/img/avatorImages/" + fileName;
        try {
            avatorFile.transferTo(dest);
            Consumer consumer = new Consumer();
            consumer.setId(id);
            consumer.setAvator(imgPath);
            boolean res = consumerService.updateUserAvator(consumer);
            if (res) {
                return message.success("上传成功", imgPath);
            } else {
                return message.error("上传失败");
            }
        } catch (IOException e) {
            return message.fatal("上传失败" + e.getMessage());
        }
    }
}
