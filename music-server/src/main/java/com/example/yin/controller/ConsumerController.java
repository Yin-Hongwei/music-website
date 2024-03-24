package com.example.yin.controller;

import com.example.yin.common.R;
import com.example.yin.model.domain.Consumer;
import com.example.yin.model.domain.Order;
import com.example.yin.model.domain.ResetPasswordRequest;
import com.example.yin.model.request.ConsumerRequest;
import com.example.yin.service.ConsumerService;
import com.example.yin.service.impl.ConsumerServiceImpl;
import com.example.yin.service.impl.SimpleOrderManager;
import com.example.yin.utils.RandomUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.util.concurrent.TimeUnit;

@RestController
public class ConsumerController {

    @Autowired
    private ConsumerService consumerService;

    @Autowired
    ConsumerServiceImpl consumerServiceimpl;

    @Autowired
    private SimpleOrderManager simpleOrderManager;

    @Autowired
    StringRedisTemplate stringRedisTemplate;
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
     * email登录
     */
    @PostMapping("/user/email/status")
    public R loginEmailStatus(@RequestBody ConsumerRequest loginRequest, HttpSession session) {
        return consumerService.loginEmailStatus(loginRequest, session);
    }

    /**
     * 密码恢复（忘记密码）
     */

    @PostMapping("/user/resetPassword")
    public R resetPassword(@RequestBody ResetPasswordRequest passwordRequest){
        Consumer user = consumerService.findByEmail(passwordRequest.getEmail());
        String code = stringRedisTemplate.opsForValue().get("code");
        if (user==null){
            return R.fatal("用户不存在");
        }else if (!code.equals(passwordRequest.getCode())){
            return R.fatal("验证码不存在或失效");
        }
        ConsumerRequest consumerRequest=new ConsumerRequest();
        BeanUtils.copyProperties(user, consumerRequest);
        System.out.println(user);
        System.out.println(consumerRequest);
        consumerRequest.setPassword(passwordRequest.getPassword());
        consumerServiceimpl.updatePassword01(consumerRequest);

        return R.success("密码修改成功");
    }

    /**
     * 发送验证码功能
     */
    @GetMapping("/user/sendVerificationCode")
    public R sendCode(@RequestParam String email){
        Consumer user = consumerService.findByEmail(email);
        if (user==null){
            return R.fatal("用户不存在");
        }
        String code = RandomUtils.code();
        simpleOrderManager.sendCode(code,email);
        //保存在redis中
        stringRedisTemplate.opsForValue().set("code",code,5, TimeUnit.MINUTES);
        return R.success("发送成功");
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
