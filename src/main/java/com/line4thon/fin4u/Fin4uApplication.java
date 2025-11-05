package com.line4thon.fin4u;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class Fin4uApplication {

	public static void main(String[] args) {
		SpringApplication.run(Fin4uApplication.class, args);
	}

}
