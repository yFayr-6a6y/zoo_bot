package com.example.zoo_bot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ZooBotApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZooBotApplication.class, args);
		System.out.println("✅ Zoo Bot успешно запущен! Бот работает в режиме Long Polling.");
	}
}