package com.example.careflow_backend.service;

import com.example.careflow_backend.Entity.PaymentDetailsEntity;
import com.example.careflow_backend.repository.PaymentDetailsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RevenueService {

    @Autowired
    private PaymentDetailsRepo paymentDetailsRepository;

    public Map<String, Object> getRevenueData() {
        // Retrieve all payment records
        List<PaymentDetailsEntity> payments = paymentDetailsRepository.findAll();

        // Maps to hold revenue and chart data
        List<Map<String, Object>> revenueRecords = new ArrayList<>();
        List<String> labels = new ArrayList<>();
        List<Integer> monthlyRevenues = new ArrayList<>();
        List<Integer> lineData = new ArrayList<>();

        // Group payments by year and month
        Map<String, Integer> revenueByMonth = new HashMap<>();
        Map<String, Integer> patientCountByMonth = new HashMap<>();

        // DateTimeFormatter for month-year format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-yyyy");

        // Process each payment record
        payments.forEach(payment -> {
            // Format payment date as "MM-yyyy"
            String monthYear = payment.getPaymentDate().format(formatter);

            // Accumulate revenue for the corresponding month-year
            revenueByMonth.put(monthYear, revenueByMonth.getOrDefault(monthYear, 0) + payment.getAmountPaid());

            // Count the number of payments (patients) for each month
            patientCountByMonth.put(monthYear, patientCountByMonth.getOrDefault(monthYear, 0) + 1);
        });

        // Populate revenue records, labels, and chart data
        revenueByMonth.forEach((monthYear, totalRevenue) -> {
            // Get the number of patients for the month
            int patientCount = patientCountByMonth.get(monthYear);

            revenueRecords.add(Map.of(
                    "monthYear", monthYear,
                    "revenue", totalRevenue,
                    "date", monthYear, // paymentDate as date
                    "number", patientCount // number of patients as number
            ));
            labels.add(monthYear);  // Month-Year as label for both charts
            monthlyRevenues.add(totalRevenue);  // Bar chart data (monthly revenue)
            lineData.add(totalRevenue);  // Line chart data (monthly revenue)
        });

        // Prepare the response map
        Map<String, Object> response = new HashMap<>();
        response.put("revenue", revenueRecords);
        response.put("lineChart", Map.of("labels", labels, "data", lineData));
        response.put("barChart", Map.of("labels", labels, "data", monthlyRevenues));

        return response;
    }
}
