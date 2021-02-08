package com.engro.paperlessbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableAutoConfiguration
@EnableJpaAuditing
@SpringBootApplication
@EnableScheduling
public class PaperlessBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(PaperlessBackendApplication.class, args);
	}

}
