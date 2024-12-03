package com.example.careflow_backend.service;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class NotificationService {
    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    // Inject Notify.lk credentials and URL from application.properties
    @Value("${notify.lk.api.url}")
    private String notifyApiUrl;

    @Value("${notify.lk.api.key}")
    private String notifyApiKey;

    @Value("${notify.lk.user.id}")
    private String userId;

    @Value("${notify.lk.sender.id}")
    private String senderId;

    public void sendOtp(String phoneNumber, String otp) {
        log.info("[NotificationService:sendOtp] Sending OTP {} to phone number {}", otp, phoneNumber);

        try {
            // Build the Notify.lk request payload
            String requestPayload = String.format(
                    "user_id=%s&api_key=%s&sender_id=%s&to=%s&message=%s",
                    userId, // Replace with your Notify.lk user ID
                    notifyApiKey,
                    senderId,
                    phoneNumber,
                    "Your OTP is: " + otp
            );

            // Set headers for the API call
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/x-www-form-urlencoded");

            HttpEntity<String> entity = new HttpEntity<>(requestPayload, headers);

            // Make the API call using RestTemplate
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.exchange(
                    notifyApiUrl,
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            // Log the response from Notify.lk
            log.info("[NotificationService:sendOtp] Notify.lk Response: {}", response.getBody());
        } catch (Exception e) {
            log.error("[NotificationService:sendOtp] Error sending OTP: {}", e.getMessage());
            throw new RuntimeException("Failed to send OTP via Notify.lk");
        }
    }

    public void sendMessage(String phoneNumber, String message) {
        log.info("[NotificationService:sendMessage] Sending message to phone number {}", phoneNumber);

        try {
            // Build the Notify.lk request payload
            String requestPayload = String.format(
                    "user_id=%s&api_key=%s&sender_id=%s&to=%s&message=%s",
                    userId,
                    notifyApiKey,
                    senderId,
                    phoneNumber,
                    message
            );

            // Set headers for the API call
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/x-www-form-urlencoded");

            HttpEntity<String> entity = new HttpEntity<>(requestPayload, headers);

            // Make the API call using RestTemplate
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.exchange(
                    notifyApiUrl,
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            // Log the response from Notify.lk
            log.info("[NotificationService:sendMessage] Notify.lk Response: {}", response.getBody());
        } catch (Exception e) {
            log.error("[NotificationService:sendMessage] Error sending message: {}", e.getMessage());
            throw new RuntimeException("Failed to send message via Notify.lk");
        }
    }

    public String formatPhoneNumber(String mobileNumber) {
        // Check if the number starts with '0', and replace it with '+94'
        if (mobileNumber.startsWith("0")) {
            return "+94" + mobileNumber.substring(1);
        }
        // If the number is already in correct format (e.g., +94776750158), return as is
        return mobileNumber;
    }
}


