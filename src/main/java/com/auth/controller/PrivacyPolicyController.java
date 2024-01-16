package com.auth.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Controller
@RequestMapping("/api/user")
public class PrivacyPolicyController {

    public static final String FILE_NAME = "Grievance Policy- Revised.pdf";

    @GetMapping("/privacy-policy")
    public ResponseEntity<String> getPrivacyPolicy() throws IOException {
        // Load the HTML content from the file
        Resource resource = new ClassPathResource("templates/policy.html");
        Path filePath = resource.getFile().toPath();
        String htmlContent = new String(Files.readAllBytes(filePath));

        // Set the Content-Type to text/html
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_HTML)
                .body(htmlContent);
    }
    @GetMapping("/terms-conditions")
    public ResponseEntity<String> getTermsAndConditions() throws IOException {
        // Load the HTML content from the file
        Resource resource = new ClassPathResource("templates/terms_and_conditions.html");
        Path filePath = resource.getFile().toPath();
        String htmlContent = new String(Files.readAllBytes(filePath));

        // Set the Content-Type to text/html
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_HTML)
                .body(htmlContent);
    }



    @GetMapping("/policy")
    public ResponseEntity<Resource> viewPdf() {
        try {
            Resource resource = new ClassPathResource("static/" + FILE_NAME);
            if (resource.exists()) {
                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + FILE_NAME);
                return ResponseEntity.ok()
                        .headers(headers)

                        .contentLength(resource.contentLength())
                        .contentType(MediaType.APPLICATION_PDF)
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            // Handle exceptions, e.g., file not found
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

