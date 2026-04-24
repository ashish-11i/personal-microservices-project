package com.ashish.eruke_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class ErukeServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ErukeServerApplication.class, args);
	}

}
