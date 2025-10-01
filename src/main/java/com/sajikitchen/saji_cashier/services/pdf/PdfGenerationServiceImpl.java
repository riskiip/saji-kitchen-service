package com.sajikitchen.saji_cashier.services.pdf;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.sajikitchen.saji_cashier.models.Order;
import com.sajikitchen.saji_cashier.models.OrderItem;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Service
public class PdfGenerationServiceImpl implements PdfGenerationService {

    @Override
    public byte[] generateOrderReceipt(Order order) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A7); // Ukuran kertas kecil untuk struk

        try {
            PdfWriter.getInstance(document, outputStream);
            document.open();

            // Fonts
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10);
            Font bodyFont = FontFactory.getFont(FontFactory.HELVETICA, 8);
            Font totalFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9);

            // Header
            document.add(new Paragraph("Saji Kitchen", headerFont));
            document.add(new Paragraph("Bukti Pesanan", bodyFont));
            document.add(new Paragraph("------------------------", bodyFont));

            // Order Details
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm");
            document.add(new Paragraph("ID: " + order.getOrderId(), bodyFont));
            document.add(new Paragraph(
                    "Tanggal: " + order.getOrderDate().atZoneSameInstant(ZoneId.of("Asia/Jakarta")).format(formatter),
                    bodyFont
            ));
            document.add(new Paragraph("Kasir: " + order.getUser().getUsername(), bodyFont));
            document.add(Chunk.NEWLINE);

            // Items Table
            PdfPTable table = new PdfPTable(3);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{3, 1, 2});

            table.addCell(createCell("Item", bodyFont, Element.ALIGN_LEFT));
            table.addCell(createCell("Qty", bodyFont, Element.ALIGN_CENTER));
            table.addCell(createCell("Harga", bodyFont, Element.ALIGN_RIGHT));

            // Table Body
            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
            currencyFormat.setMinimumFractionDigits(0);
            currencyFormat.setMaximumFractionDigits(0);

            for (OrderItem item : order.getOrderItems()) {
                String itemName = item.getProductVariant().getProduct().getName() + " (" + item.getProductVariant().getName() + ")";
                if (item.getTopping() != null) {
                    itemName += " + " + item.getTopping().getName();
                }

                table.addCell(createCell(itemName, bodyFont, Element.ALIGN_LEFT));
                table.addCell(createCell(String.valueOf(item.getQuantity()), bodyFont, Element.ALIGN_CENTER));

                var subtotal = item.getPriceAtPurchase().add(
                        item.getToppingPriceAtPurchase() != null ? item.getToppingPriceAtPurchase() : java.math.BigDecimal.ZERO
                ).multiply(new java.math.BigDecimal(item.getQuantity()));

                table.addCell(createCell(currencyFormat.format(subtotal), bodyFont, Element.ALIGN_RIGHT));
            }
            document.add(table);

            // Total
            document.add(Chunk.NEWLINE);
            Paragraph total = new Paragraph("Total: " + currencyFormat.format(order.getTotalAmount()), totalFont);
            total.setAlignment(Element.ALIGN_RIGHT);
            document.add(total);

            // Footer
            document.add(Chunk.NEWLINE);
            Paragraph footer = new Paragraph("Terima kasih!", bodyFont);
            footer.setAlignment(Element.ALIGN_CENTER);
            document.add(footer);

        } catch (DocumentException e) {
            throw new IOException("Error generating PDF", e);
        } finally {
            document.close();
        }

        return outputStream.toByteArray();
    }

    private PdfPCell createCell(String content, Font font, int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(content, font));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(alignment);
        return cell;
    }
}