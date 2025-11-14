package com.line4thon.fin4u;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableCaching
@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
public class Fin4uApplication {

	public static void main(String[] args) {
		SpringApplication.run(Fin4uApplication.class, args);
	}

}
