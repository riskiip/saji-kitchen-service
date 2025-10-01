package com.sajikitchen.saji_cashier.services.pdf;

import com.sajikitchen.saji_cashier.models.Order;
import java.io.IOException;

public interface PdfGenerationService {
    byte[] generateOrderReceipt(Order order) throws IOException;
}