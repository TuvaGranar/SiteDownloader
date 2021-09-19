package com.example.SiteDownloader.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "sitedownloader")
@Data
public class AppConfig {

    private String baseUrl;
    private int timeout;
    private int forkPoolThreshold;
    private String baseDir;
}
