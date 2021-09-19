package com.example.SiteDownloader;

import com.example.SiteDownloader.service.DownloadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class SiteDownloaderApplication implements CommandLineRunner {

	@Autowired
	private DownloadService downloadService;

	public static void main(String[] args) {
		SpringApplication.run(SiteDownloaderApplication.class, args);
	}

	@Override
	public void run(String... args) {
		System.out.println("EXECUTING : command line runner");

		for (int i = 0; i < args.length; ++i) {
			System.out.println(String.format("args[%s]: %s", i, args[i]));
		}
		downloadService.traverseWebPage();

	}

	/** Bean definition **/
	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}
}
