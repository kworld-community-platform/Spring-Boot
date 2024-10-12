package com.hyunjin.kworld;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class KworldApplication {

	public static void main(String[] args) {
		SpringApplication.run(KworldApplication.class, args);
	}

}
