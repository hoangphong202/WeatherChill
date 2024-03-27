package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
//(exclude = SecurityAutoConfiguration.class)
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
//@SpringBootApplication
public class WeatherChill2Application {

	public static void main(String[] args) {
		SpringApplication.run(WeatherChill2Application.class, args);
	}

}
