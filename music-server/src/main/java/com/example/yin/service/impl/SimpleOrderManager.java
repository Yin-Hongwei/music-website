package com.example.yin.service.impl;
 
import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import com.example.yin.model.domain.Order;
import com.example.yin.service.OrderManager;
import com.example.yin.utils.RandomUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * 邮箱信息的发送
 */

@Service
public class SimpleOrderManager implements OrderManager {

    @Value("${mail.address}")
    private String sendAddress;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private StringRedisTemplate redisTemplate;

    public void setMailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendPassword(Order order, String reciveAddress) {
        MimeMessagePreparator preparator = new MimeMessagePreparator() {
            public void prepare(@NotNull MimeMessage mimeMessage) throws Exception {
                mimeMessage.setRecipient(Message.RecipientType.TO,
                        new InternetAddress(reciveAddress));
                mimeMessage.setFrom(new InternetAddress(sendAddress));
                mimeMessage.setText("Dear " + order.getName() + "\n" +
                        "Your  password is :" + order.getPassword() + ".");
            }
        };

        try {
            this.mailSender.send(preparator);
        } catch (MailException ex) {

            System.err.println(ex.getMessage());
        }
    }

    public void sendCode(String reciveAddress) {
        String code = RandomUtils.code();
        MimeMessagePreparator preparator = new MimeMessagePreparator() {
            public void prepare(@NotNull MimeMessage mimeMessage) throws Exception {
                mimeMessage.setRecipient(Message.RecipientType.TO,
                        new InternetAddress(reciveAddress));
                mimeMessage.setFrom(new InternetAddress(sendAddress));
                mimeMessage.setText("Dear you code is " + code);
            }
        };
        try {
            this.mailSender.send(preparator);
        } catch (MailException e) {
            System.err.println(e.getMessage());
        }
        //将验证码保存在redis中
        redisTemplate.opsForValue().set("code",code,5, TimeUnit.MINUTES);
    }
}