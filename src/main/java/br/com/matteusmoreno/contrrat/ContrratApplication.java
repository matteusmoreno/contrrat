package br.com.matteusmoreno.contrrat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ContrratApplication {

	public static void main(String[] args) {
		SpringApplication.run(ContrratApplication.class, args);
	}

}
