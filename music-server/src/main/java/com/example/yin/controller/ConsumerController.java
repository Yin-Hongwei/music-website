package com.example.yin.controller;

import com.example.yin.common.R;
import com.example.yin.constant.Constants;
import com.example.yin.domain.Consumer;
import com.example.yin.request.ConsumerRequest;
import com.example.yin.service.impl.ConsumerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
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


    //TODO 这个配置类的目的是什么
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
    @PostMapping("/user/add")
    public R addUser(HttpServletRequest req) {
        return consumerService.addUser(req);
    }

    /**
     * TODO 前台页面调用
     * 登录判断
     */
    @PostMapping("/user/login/status")
    public R loginStatus(@RequestBody ConsumerRequest loginRequest, HttpSession session) {

        return consumerService.loginStatus(loginRequest);
    }

    /**
     * 返回所有用户
     */
    @GetMapping("/user")
    public R allUser() {
        return R.success(null, consumerService.allUser());
    }

    //TODO ok
    /**
     * 返回指定 ID 的用户
     */
    @GetMapping("/user/detail")
    public R userOfId(HttpServletRequest req) {
        String id = req.getParameter("id");
        //没问题的 既然登录进来了 那么肯定存在
        return R.success(null, consumerService.userOfId(Integer.parseInt(id)));
    }

    /**
     * 删除用户
     */
    @GetMapping("/user/delete")
    public R deleteUser(HttpServletRequest req) {
        String id = req.getParameter("id");

        boolean res = consumerService.deleteUser(Integer.parseInt(id));
        if (res) {
            return R.success("删除成功");
        } else {
            return R.error("删除失败");
        }
    }

    /**
     * 更新用户信息
     */
    @PostMapping("/user/update")
    public R updateUserMsg(HttpServletRequest req) {
        String id = req.getParameter("id").trim();
        String username = req.getParameter("username").trim();
        String sex = req.getParameter("sex").trim();
        String phoneNum = req.getParameter("phone_num").trim();
        String email = req.getParameter("email").trim();
        String birth = req.getParameter("birth").trim();
        String introduction = req.getParameter("introduction").trim();
        String location = req.getParameter("location").trim();

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
            return R.success("修改成功");
        } else {
            return R.error("修改失败");
        }
    }

    /**
     * 更新用户密码
     */
    @PostMapping("/user/updatePassword")
    public R updatePassword(HttpServletRequest req) {
        String id = req.getParameter("id").trim();
        String username = req.getParameter("username").trim();
        String oldPassword = req.getParameter("old_password").trim();
        String password = req.getParameter("password").trim();

        boolean res = consumerService.verityPasswd(username, oldPassword);
        if (!res) {
            return R.error("密码输入错误");
        }

        Consumer consumer = new Consumer();
        consumer.setId(Integer.parseInt(id));
        consumer.setPassword(password);

        boolean result = consumerService.updatePassword(consumer);
        if (result) {
            return R.success("密码修改成功");
        } else {
            return R.error("密码修改失败");
        }
    }

    /**
     * 更新用户头像
     */
    @PostMapping("/user/avatar/update")
    public R updateUserPic(@RequestParam("file") MultipartFile avatorFile, @RequestParam("id") int id) {
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
                return R.success("上传成功", imgPath);
            } else {
                return R.error("上传失败");
            }
        } catch (IOException e) {
            return R.fatal("上传失败" + e.getMessage());
        }
    }
}
