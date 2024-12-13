package com.example.Event_Project_Spring.service;


import com.example.Event_Project_Spring.entities.User;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;



@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendVerificationEmail(User user, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Email Verification");
        message.setText("Please verify your email using this link: http://localhost:8080/user/verify-email?token=" + token);
        try {
            mailSender.send(message);
            System.out.println("Email successfully sent to " + user.getEmail());
        } catch (MailException e) {
            System.err.println("Failed to send email to " + user.getEmail() + ": " + e.getMessage());
            e.printStackTrace();
        }
    }
}

