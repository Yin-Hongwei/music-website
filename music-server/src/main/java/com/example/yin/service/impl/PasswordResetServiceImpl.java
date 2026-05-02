package com.example.yin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.yin.common.R;
import com.example.yin.mapper.AppUserMapper;
import com.example.yin.model.domain.AppUser;
import com.example.yin.model.request.PasswordResetRequest;
import com.example.yin.service.PasswordResetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class PasswordResetServiceImpl implements PasswordResetService {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");

    private static final String REDIS_CODE_PREFIX = "yin:pwd-reset:code:";
    private static final String REDIS_COOL_PREFIX = "yin:pwd-reset:cool:";

    private final StringRedisTemplate stringRedisTemplate;
    private final AppUserMapper appUserMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired(required = false)
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username:}")
    private String springMailUsername;

    @Value("${app.password-reset.code-ttl-minutes:10}")
    private int codeTtlMinutes;

    @Value("${app.password-reset.send-cooldown-seconds:60}")
    private int sendCooldownSeconds;

    @Value("${app.password-reset.expose-code:false}")
    private boolean exposeCode;

    /** 本地开发：不配 SMTP 也发码，接口 data.debugCode 直出（生产务必 false） */
    @Value("${app.password-reset.mock-without-mail:false}")
    private boolean mockWithoutMail;

    private static final String DEFAULT_MAIL_SUBJECT = "【音乐网站】找回密码验证码";

    @Value("${app.password-reset.mail-subject:}")
    private String configuredMailSubject;

    private String normalizeEmail(String email) {
        if (email == null) {
            return "";
        }
        return email.trim().toLowerCase();
    }

    private AppUser findByEmail(String emailNorm) {
        QueryWrapper<AppUser> q = new QueryWrapper<>();
        q.eq("email", emailNorm);
        return appUserMapper.selectOne(q);
    }

    private void sendResetCodeEmail(String to, String code) throws MessagingException {
        if (javaMailSender == null) {
            throw new IllegalStateException("JavaMailSender not configured");
        }
        String from = StringUtils.trimToEmpty(springMailUsername);
        if (StringUtils.isBlank(from)) {
            throw new IllegalStateException("spring.mail.username is empty");
        }

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom(from);
        helper.setTo(to);
        helper.setSubject(StringUtils.isNotBlank(configuredMailSubject) ? configuredMailSubject : DEFAULT_MAIL_SUBJECT);
        helper.setText(buildHtmlBody(code, codeTtlMinutes), true);
        javaMailSender.send(message);
    }

    private String buildHtmlBody(String code, int ttlMinutes) {
        return "<html><body style=\"font-family:sans-serif;line-height:1.6;color:#333;\">"
                + "<p>您正在重置登录密码，验证码为：</p>"
                + "<p style=\"font-size:22px;font-weight:600;letter-spacing:4px;color:#409EFF;\">"
                + code
                + "</p>"
                + "<p style=\"color:#909399;font-size:13px;\">"
                + "验证码 "
                + ttlMinutes
                + " 分钟内有效，请勿泄露给他人。如非本人操作，请忽略本邮件。</p>"
                + "</body></html>";
    }

    private void rollbackAfterSendFailure(String codeKey, String coolKey) {
        stringRedisTemplate.delete(codeKey);
        stringRedisTemplate.delete(coolKey);
    }

    @Override
    public R sendVerificationCode(String email) {
        String emailNorm = normalizeEmail(email);
        if (StringUtils.isBlank(emailNorm) || !EMAIL_PATTERN.matcher(emailNorm).matches()) {
            return R.warning("请输入正确的邮箱地址");
        }
        String coolKey = REDIS_COOL_PREFIX + emailNorm;
        if (Boolean.TRUE.equals(stringRedisTemplate.hasKey(coolKey))) {
            return R.warning("发送过于频繁，请稍后再试");
        }

        stringRedisTemplate.opsForValue().set(coolKey, "1", sendCooldownSeconds, TimeUnit.SECONDS);

        AppUser user = findByEmail(emailNorm);
        if (user == null) {
            log.info("Password reset code requested for unknown email (handled silently)");
            stringRedisTemplate.delete(coolKey);
            return R.success("若该邮箱已注册，验证码将用于重置密码");
        }

        String code = String.format("%06d", ThreadLocalRandom.current().nextInt(1_000_000));
        String codeKey = REDIS_CODE_PREFIX + emailNorm;
        stringRedisTemplate.opsForValue().set(codeKey, code, codeTtlMinutes, TimeUnit.MINUTES);

        if (mockWithoutMail) {
            log.warn("Password reset code issued without email (mock-without-mail) for {}", emailNorm);
            return R.success("本地开发：未发邮件，请使用页面上的验证码", Collections.singletonMap("debugCode", code));
        }

        if (javaMailSender == null) {
            stringRedisTemplate.delete(codeKey);
            stringRedisTemplate.delete(coolKey);
            return R.fatal("系统未配置发件邮箱（spring.mail.host 等），请联系管理员");
        }
        if (StringUtils.isBlank(StringUtils.trimToEmpty(springMailUsername))) {
            stringRedisTemplate.delete(codeKey);
            stringRedisTemplate.delete(coolKey);
            return R.fatal("系统未配置发件人（spring.mail.username），请联系管理员");
        }

        try {
            sendResetCodeEmail(emailNorm, code);
            log.info("Password reset verification email sent to {}", emailNorm);
        } catch (Exception e) {
            log.error("Failed to send password reset email to {}", emailNorm, e);
            rollbackAfterSendFailure(codeKey, coolKey);
            if (e instanceof IllegalStateException) {
                return R.fatal(e.getMessage());
            }
            if (e instanceof MailException || e instanceof MessagingException) {
                return R.error("邮件发送失败，请稍后重试或检查邮箱是否可用");
            }
            return R.error("邮件发送失败，请稍后重试");
        }

        Object data = exposeCode ? Collections.singletonMap("debugCode", code) : null;
        return R.success("验证码已发送至您的邮箱，请查收（含垃圾箱）", data);
    }

    @Override
    public R resetPassword(PasswordResetRequest request) {
        if (request == null) {
            return R.error("请求体无效");
        }
        String emailNorm = normalizeEmail(request.getEmail());
        if (StringUtils.isBlank(emailNorm) || !EMAIL_PATTERN.matcher(emailNorm).matches()) {
            return R.warning("请输入正确的邮箱地址");
        }
        String code = StringUtils.trimToEmpty(request.getCode());
        if (code.length() != 6 || !code.chars().allMatch(Character::isDigit)) {
            return R.warning("请输入6位数字验证码");
        }
        String password = request.getPassword();
        String confirm = request.getConfirmPassword();
        if (StringUtils.isBlank(password) || password.length() < 6) {
            return R.warning("新密码至少6位");
        }
        if (!password.equals(confirm)) {
            return R.warning("两次输入的密码不一致");
        }

        String codeKey = REDIS_CODE_PREFIX + emailNorm;
        String stored = stringRedisTemplate.opsForValue().get(codeKey);
        if (stored == null || !stored.equals(code)) {
            return R.error("验证码错误或已过期");
        }

        AppUser user = findByEmail(emailNorm);
        if (user == null) {
            return R.error("该邮箱未注册");
        }

        AppUser update = new AppUser();
        update.setId(user.getId());
        update.setPassword(passwordEncoder.encode(password));
        if (appUserMapper.updateById(update) > 0) {
            stringRedisTemplate.delete(codeKey);
            stringRedisTemplate.delete(REDIS_COOL_PREFIX + emailNorm);
            return R.success("密码已重置，请使用新密码登录");
        }
        return R.error("重置失败，请稍后重试");
    }
}
