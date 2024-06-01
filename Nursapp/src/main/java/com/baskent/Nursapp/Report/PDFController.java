 package com.baskent.Nursapp.Report;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Base64;

 @RestController
@RequestMapping("/nursapp/app/report")
public class PDFController {

    private final PDFService pdfService;

    @Autowired
    public PDFController(PDFService pdfService) {
        this.pdfService = pdfService;
    }

    @GetMapping("/download")
    public ResponseEntity<String> downloadPDF(@RequestParam Long studentId, @RequestParam Long bolumId) throws IOException {
        // studentId parametresini kullanarak PDF oluştur
        byte[] pdfBytes = pdfService.createPDF(studentId, bolumId);

        // Byte array'ı Base64 string'ine dönüştür
        String base64Encoded = Base64.getEncoder().encodeToString(pdfBytes);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=\"report.pdf\"");
        headers.add("Cache-Control", "must-revalidate, post-check=0, pre-check=0");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(base64Encoded);
    }
}