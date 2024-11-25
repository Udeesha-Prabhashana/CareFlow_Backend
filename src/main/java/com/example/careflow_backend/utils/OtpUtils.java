package com.example.careflow_backend.utils;

import java.time.LocalDateTime;
import java.util.Random;

public class OtpUtils {
    public static String generateOtp() {
        return String.valueOf(new Random().nextInt(900000) + 100000); // 6-digit OTP
    }

    public static LocalDateTime generateOtpExpiry() {
        return LocalDateTime.now().plusMinutes(5); // OTP valid for 5 minutes
    }
}
