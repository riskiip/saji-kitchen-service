package com.sajikitchen.saji_cashier.services.email;

import com.sajikitchen.saji_cashier.models.Order;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailServiceImpl.class);

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String mailFrom;

    public EmailServiceImpl(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    @Override
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
            String htmlContent = templateEngine.process("receipt-template", context); // Sesuaikan path jika perlu
            helper.setText(htmlContent, true);

            // Lampirkan PDF
            helper.addAttachment("Struk-" + order.getOrderId() + ".pdf", new ByteArrayResource(pdfAttachment));

            mailSender.send(mimeMessage);
            log.info("Confirmation email sent successfully to {} for order {}", to, order.getOrderId());

        } catch (MessagingException e) {
            log.error("Failed to send confirmation email for order {}: {}", order.getOrderId(), e.getMessage());
            // Lemparkan exception custom agar transaksi bisa di-rollback jika diperlukan,
            // atau tangani sesuai kebutuhan bisnis.
            throw new RuntimeException("Error sending email", e);
        }
    }
}