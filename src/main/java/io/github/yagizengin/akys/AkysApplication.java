package io.github.yagizengin.akys;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AkysApplication {

	public static void main(String[] args) {
		SpringApplication.run(AkysApplication.class, args);
	}

}
