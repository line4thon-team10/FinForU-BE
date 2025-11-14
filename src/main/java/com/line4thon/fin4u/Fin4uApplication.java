package com.line4thon.fin4u;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableCaching
@SpringBootApplication
@EnableJpaAuditing
public class Fin4uApplication {

	public static void main(String[] args) {
		SpringApplication.run(Fin4uApplication.class, args);
	}

}
