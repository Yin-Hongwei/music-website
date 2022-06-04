package com.example.yin.service.impl;

import com.example.yin.common.Message;
import com.example.yin.dao.ConsumerMapper;
import com.example.yin.domain.Consumer;
import com.example.yin.service.ConsumerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class ConsumerServiceImpl implements ConsumerService {

    @Autowired
    private ConsumerMapper consumerMapper;


    private Message message = new Message();

    /**
     * 新增用户
     */
    @Override
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

        if (this.existUser(username)) {
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
            boolean res = consumerMapper.insertSelective(consumer)>0;
            if (res) {
                return message.success("注册成功");
            } else {
                return message.error("注册失败");
            }
        } catch (DuplicateKeyException e) {
            return message.fatal(e.getMessage());
        }
    }

    @Override
    public boolean updateUserMsg(Consumer consumer) {
        return consumerMapper.updateUserMsg(consumer) > 0;
    }

    @Override
    public boolean updatePassword(Consumer consumer) {
        return consumerMapper.updatePassword(consumer) > 0;
    }

    @Override
    public boolean updateUserAvator(Consumer consumer) {
        return consumerMapper.updateUserAvator(consumer) > 0;
    }

    @Override
    public boolean existUser(String username) {
        return consumerMapper.existUsername(username) > 0;
    }

    @Override
    public boolean verityPasswd(String username, String password) {
        return consumerMapper.verifyPassword(username, password) > 0;
    }

    // 删除用户
    @Override
    public boolean deleteUser(Integer id) {
        return consumerMapper.deleteUser(id) > 0;
    }

    @Override
    public List<Consumer> allUser() {
        return consumerMapper.allUser();
    }

    @Override
    public List<Consumer> userOfId(Integer id) {
        return consumerMapper.userOfId(id);
    }

    @Override
    public List<Consumer> loginStatus(String username) {
        return consumerMapper.loginStatus(username);
    }
}
