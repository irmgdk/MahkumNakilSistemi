// NakilSistemiApplication.java
package com.example.akilli.mahkum.nakil_sistemi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling  // GPSKonumService'de Scheduled metodlar için
public class NakilSistemiApplication {

	public static void main(String[] args) {
		SpringApplication.run(NakilSistemiApplication.class, args);
	}
}