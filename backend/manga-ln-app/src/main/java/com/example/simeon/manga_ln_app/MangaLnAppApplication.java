package com.example.simeon.manga_ln_app;

import io.github.cdimascio.dotenv.Dotenv;
import  org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MangaLnAppApplication {

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.configure()
				.directory("src/main/resources")
				.load();
		System.setProperty("DATABASE_URL", dotenv.get("DATABASE_URL"));
		System.setProperty("DATABASE_USERNAME", dotenv.get("DATABASE_USERNAME"));
		System.setProperty("DATABASE_PASSWORD", dotenv.get("DATABASE_PASSWORD"));
		System.setProperty("STORAGE_BUCKET_URL", dotenv.get("STORAGE_BUCKET_URL"));
		System.setProperty("STORAGE_API_KEY", dotenv.get("STORAGE_API_KEY"));
		SpringApplication.run(MangaLnAppApplication.class, args);
	}

}
