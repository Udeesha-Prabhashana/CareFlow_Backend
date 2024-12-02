package com.example.careflow_backend.controller;

import com.example.careflow_backend.service.RevenueService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping({"/api", "/"})
@RequiredArgsConstructor
@Slf4j
@CrossOrigin("http://localhost:3000")
public class RevenueController {

    @Autowired
    private RevenueService revenueService;

    @GetMapping("/revenue")
    public ResponseEntity<Map<String, Object>> getRevenueData() {
        Map<String, Object> data = revenueService.getRevenueData();
        return ResponseEntity.ok(data);
    }
}

