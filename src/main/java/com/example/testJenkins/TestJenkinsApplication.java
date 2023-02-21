package com.example.testJenkins;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class TestJenkinsApplication {

	public static void main(String[] args) {
		SpringApplication.run(TestJenkinsApplication.class, args);
		System.out.println("555");
	}

}
