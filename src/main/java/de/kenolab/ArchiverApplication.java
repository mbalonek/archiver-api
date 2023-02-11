package de.kenolab;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.core.userdetails.User;


@SpringBootApplication
public class ArchiverApplication {

	public static void main(String[] args) {
		SpringApplication.run(ArchiverApplication.class, args);

	}


}
