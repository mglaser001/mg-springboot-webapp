package com.springboot.app.mail;

public interface IEmailSender {
    void send(String to, String email);
}
