package com.example.mutbooks.app.mail;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

/**
 * JavaMailSender 메일발송을 위한 MailConfig 설정 참고
 * @See <a href="https://gist.github.com/ihoneymon/56dd964336322eea04dc">참고1</a>
 * @See <a href="https://velog.io/@coffiter/SpringBoot-%EB%A9%94%EC%9D%BC-%EB%B0%9C%EC%86%A1JavaMailSender">참고2</a>
 */
@Configuration
public class MailConfig {
    private static final String MAIL_SMTP_AUTH = "mail.smtp.auth";
    private static final String MAIL_SMTP_STARTTLS_ENABLE = "mail.smtp.starttls.enable";

    @Value("${spring.mail.properties.mail.smtp.auth}")
    private boolean auth;

    @Value("${spring.mail.properties.mail.smtp.starttls.enable}")
    private boolean starttls;

    @Value("${spring.mail.protocol}")
    private String protocol;

    @Value("${spring.mail.host}")
    private String host;        // smtp 서버

    @Value("${spring.mail.port}")
    private int port;           // 포트번호

    @Value("${spring.mail.username}")
    private String username;    // 계정

    @Value("${spring.mail.password}")
    private String password;    // 비밀번호

    @Value("${spring.mail.default-encoding}")
    private String defaultEncoding;

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        Properties properties = mailSender.getJavaMailProperties();
        properties.put(MAIL_SMTP_AUTH, auth);
        properties.put(MAIL_SMTP_STARTTLS_ENABLE, starttls);
        mailSender.setJavaMailProperties(properties);

        mailSender.setProtocol(protocol);
        mailSender.setHost(host);
        mailSender.setPort(port);
        mailSender.setUsername(username);
        mailSender.setPassword(password);
        mailSender.setDefaultEncoding(defaultEncoding);

        return mailSender;
    }
}
