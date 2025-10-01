package com.sajikitchen.saji_cashier.services.email;

import com.sajikitchen.saji_cashier.models.Order;

public interface EmailService {
    void sendOrderConfirmationEmail(String to, Order order, byte[] pdfAttachment);
}