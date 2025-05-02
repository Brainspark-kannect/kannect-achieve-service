package com.kannect.achieve;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@ComponentScan(basePackages = "com.kannect")
@EnableJpaRepositories(basePackages = {"com.kannect.user.auth.repository","com.kannect.achieve.repository"})
@EnableFeignClients(basePackages = "com.kannect.achieve.service")
@EntityScan(basePackages = {"com.kannect.user.auth.entity","com.kannect.achieve.entity"})
public class KannectAchieveServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(KannectAchieveServiceApplication.class, args);
	}
}
