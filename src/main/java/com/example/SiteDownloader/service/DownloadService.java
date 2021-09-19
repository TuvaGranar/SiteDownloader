package com.example.SiteDownloader.service;

import com.example.SiteDownloader.configuration.AppConfig;
import org.springframework.stereotype.Service;

import java.util.concurrent.ForkJoinPool;

@Service
public class DownloadService {

    private AppConfig appConfig;
    private HttpService httpService;
    private String baseUrl;
    private VisitedSites visited;

    public DownloadService(AppConfig appConfig, HttpService httpService) {
        this.appConfig = appConfig;
        this.httpService = httpService;
        this.baseUrl = appConfig.getBaseUrl();
    }

    /**
     * Gets a web page and recursively traverses the links in the html and saves them to the system.
     */
    public void traverseWebPage() {
        System.out.println("Starting download..");
        long startTime = System.currentTimeMillis();
        visited = new VisitedSites();
        visited.visit(baseUrl);

        new ForkJoinPool().invoke(
                new SiteDownloaderRecursiveTask(baseUrl, visited, httpService, baseUrl, appConfig.getForkPoolThreshold(), appConfig.getBaseDir()));


        long endTime = System.currentTimeMillis();
        long duration = endTime-startTime;
        System.out.println("Download finished, took " + duration + "ms");
        System.out.println("Number of visited pages: " + visited.getVisited().size());
    }
}
