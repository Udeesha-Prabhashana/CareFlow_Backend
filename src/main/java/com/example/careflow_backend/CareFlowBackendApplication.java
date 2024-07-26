package com.example.careflow_backend;

import com.example.careflow_backend.config.RSAKeyRecord;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(RSAKeyRecord.class)
@SpringBootApplication
public class CareFlowBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(CareFlowBackendApplication.class, args);
	}

}
