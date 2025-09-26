package com.sajikitchen.saji_cashier.services.email;

import com.sajikitchen.saji_cashier.models.Order;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String mailFrom;

    public EmailService(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    public void sendOrderConfirmationEmail(String to, Order order, byte[] pdfAttachment) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setTo(to);
            helper.setFrom(mailFrom);
            helper.setSubject("Detail Pesanan Saji Kitchen #" + order.getOrderId());

            // Proses template Thymeleaf
            Context context = new Context();
            context.setVariable("order", order);
            String htmlContent = templateEngine.process("receipt-template", context);
            helper.setText(htmlContent, true);

            // Lampirkan PDF
            helper.addAttachment("Struk-" + order.getOrderId() + ".pdf", new ByteArrayResource(pdfAttachment));

            mailSender.send(mimeMessage);

        } catch (MessagingException e) {
            // Sebaiknya gunakan logger di aplikasi nyata
            e.printStackTrace();
        }
    }
}