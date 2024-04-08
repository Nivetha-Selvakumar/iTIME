package com.example.iTIME;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
@ComponentScan("com.example.iTIME")
@EnableScheduling
public class ItimeApplication {

	public static void main(String[] args) {
		SpringApplication.run(ItimeApplication.class, args);
	}

}
