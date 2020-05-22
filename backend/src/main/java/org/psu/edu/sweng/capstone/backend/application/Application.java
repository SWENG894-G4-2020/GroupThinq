package org.psu.edu.sweng.capstone.backend.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ComponentScan(basePackages = "org.psu.edu.sweng.capstone.backend")
@EnableJpaRepositories("org.psu.edu.sweng.capstone.backend.dao")
@EntityScan("org.psu.edu.sweng.capstone.backend.model")
@SpringBootApplication
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
