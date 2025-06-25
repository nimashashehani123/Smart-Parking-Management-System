package com.example.paymentservice.utill;

import com.example.paymentservice.entity.Transaction;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.time.Duration;

public class ReceiptGenerator {

    public static byte[] generate(Transaction tx) {
        try {
            Document document = new Document();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            PdfWriter.getInstance(document, out);
            document.open();

            Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
            Font fontBody = FontFactory.getFont(FontFactory.HELVETICA, 12);

            document.add(new Paragraph("Payment Receipt", fontTitle));
            document.add(new Paragraph("--------------------------------------------------"));

            document.add(new Paragraph("Transaction ID: " + tx.getId(), fontBody));
            document.add(new Paragraph("User ID       : " + tx.getUserId(), fontBody));
            document.add(new Paragraph("Reservation ID: " + tx.getReservationId(), fontBody));
            document.add(new Paragraph("Start Time    : " + tx.getStartTime(), fontBody));
            document.add(new Paragraph("End Time      : " + tx.getEndTime(), fontBody));
            document.add(new Paragraph("Rate (LKR/hr) : " + tx.getPricePerHour(), fontBody));

            Duration duration = Duration.between(tx.getStartTime(), tx.getEndTime());
            double hoursUsed = Math.round((duration.toMinutes() / 60.0) * 100.0) / 100.0;
            document.add(new Paragraph("Duration (hrs): " + hoursUsed, fontBody));

            document.add(new Paragraph("Amount (LKR)  : " + tx.getAmount(), fontTitle));
            document.add(new Paragraph("Status        : " + tx.getStatus(), fontBody));
            document.add(new Paragraph("Date/Time     : " + tx.getTimestamp(), fontBody));

            document.close();
            return out.toByteArray();
        } catch (Exception e) {
            return null;
        }
    }
}
