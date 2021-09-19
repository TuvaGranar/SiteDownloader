package com.example.SiteDownloader.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class HttpService {

    private RestTemplate restTemplate;

    public HttpService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String fetchWebPage(String url) {
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            return response.getBody();
        } catch(Exception e) {
            System.out.println("Failed to fetch web page: " + url);
            e.printStackTrace();
            return "";
        }
    }
}
