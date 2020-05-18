package org.psu.edu.sweng.capstone.backend.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "org.psu.edu.sweng.capstone.backend")
public class Application {
	public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
	}
}
