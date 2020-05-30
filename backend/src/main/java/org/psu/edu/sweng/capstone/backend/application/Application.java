package org.psu.edu.sweng.capstone.backend.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@ComponentScan(basePackages = "org.psu.edu.sweng.capstone.backend")
@EnableJpaRepositories("org.psu.edu.sweng.capstone.backend.dao")
@EntityScan("org.psu.edu.sweng.capstone.backend.model")
@SpringBootApplication
@EnableSwagger2
public class Application {

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
