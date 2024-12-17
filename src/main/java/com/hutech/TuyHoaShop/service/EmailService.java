package com.hutech.TuyHoaShop.service;

import com.hutech.TuyHoaShop.model.Order;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.List;


@Service
public class EmailService {
    @Autowired
    private OrderService orderService;
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    public void sendSimpleEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        mailSender.send(message);
    }
    public void sendHtmlEmail(String to, String subject,  String message ) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        // Chuẩn bị dữ liệu cho template
        Context context = new Context();
        context.setVariable("subject", subject);
        context.setVariable("message", message);


        // Render template
        String htmlBody = templateEngine.process("emailTemplate", context);

        // Thiết lập email
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlBody, true); // true để định dạng HTML

        // Gửi email
        mailSender.send(mimeMessage);
    }
    public void sendOrderConfirmationEmail(String to, long orderId) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        Order order = orderService.getOrderById(orderId);
        // Chuẩn bị dữ liệu cho template
        Context context = new Context();
        context.setVariable("order", order);
        context.setVariable("customerName", order.getCustomerName());
        context.setVariable("orderId", order.getId());

        context.setVariable("orderDetails", order.getOrderDetails());


        // Render template
        String htmlBody = templateEngine.process("emailTemplate", context);

        // Thiết lập email
        helper.setTo(to);
        helper.setSubject("Xác nhận đơn hàng #" + order.getId());
        helper.setText(htmlBody, true); // true để định dạng HTML

        // Gửi email
        mailSender.send(mimeMessage);
    }



}
