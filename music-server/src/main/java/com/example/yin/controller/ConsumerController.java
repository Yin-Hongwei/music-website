package com.example.yin.controller;

import com.example.yin.common.R;
import com.example.yin.model.domain.Consumer;
import com.example.yin.model.domain.Order;
import com.example.yin.model.request.ConsumerRequest;
import com.example.yin.service.ConsumerService;
import com.example.yin.service.impl.SimpleOrderManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;

@RestController
public class ConsumerController {

    @Autowired
    private ConsumerService consumerService;

    @Autowired
    private SimpleOrderManager simpleOrderManager;

    /**
     * TODO 前台页面调用 注册
     * 用户注册
     */
    @PostMapping("/user/add")
    public R addUser(@RequestBody ConsumerRequest registryRequest) {
        return consumerService.addUser(registryRequest);
    }

    /**
     * TODO 前台页面调用  登录
     * 登录判断
     */
    @PostMapping("/user/login/status")
    public R loginStatus(@RequestBody ConsumerRequest loginRequest, HttpSession session) {
        return consumerService.loginStatus(loginRequest, session);
    }


    /**
     * 密码恢复（忘记密码）
     */

    @PostMapping("/user/forget")
    public R resetPassword(@RequestBody String email){
        String[] split = email.split(":");
        email=split[1];
        int index = email.indexOf("}");
        email = email.substring(0, index);
        // 去掉前面的双引号
        if (email.startsWith("\"")) {
            email = email.substring(1);
        }
        // 去掉后面的双引号
        if (email.endsWith("\"")) {
            email = email.substring(0, email.length() - 1);
        }

        Consumer user = consumerService.findByEmail(email);
        user.getPassword();
        if (user==null){
            return R.fatal("用户不存在");
        }
        Order order=new Order();
        order.setName(user.getUsername());
        order.setPassword(user.getPassword());
        simpleOrderManager.send(order,email);
        return R.success("密码发送成功");
    }

    /**
     * TODO 管理界面调用
     * 返回所有用户
     */
    @GetMapping("/user")
    public R allUser() {
        return consumerService.allUser();
    }


    /**
     * TODO 用户界面调用
     * 返回指定 ID 的用户
     */
    @GetMapping("/user/detail")
    public R userOfId(@RequestParam int id) {
        return consumerService.userOfId(id);
    }

    /**
     * TODO 管理界面的调用
     * 删除用户
     */
    @GetMapping("/user/delete")
    public R deleteUser(@RequestParam int id) {
        return consumerService.deleteUser(id);
    }

    /**
     * TODO 前后台界面的调用
     * 更新用户信息
     */
    @PostMapping("/user/update")
    public R updateUserMsg(@RequestBody ConsumerRequest updateRequest) {
        return consumerService.updateUserMsg(updateRequest);
    }

    /**
     * TODO 前后台更新用户的密码
     * 更新用户密码
     */
    @PostMapping("/user/updatePassword")
    public R updatePassword(@RequestBody ConsumerRequest updatePasswordRequest) {
        return consumerService.updatePassword(updatePasswordRequest);
    }

    /**
     * 更新用户头像
     */
    @PostMapping("/user/avatar/update")
    public R updateUserPic(@RequestParam("file") MultipartFile avatorFile, @RequestParam("id") int id) {
        return consumerService.updateUserAvator(avatorFile, id);
    }

}
