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

//        recursiveFetchPages(baseUrl);

        new ForkJoinPool().invoke(
                new SiteDownloaderRecursiveTask(baseUrl, visited, httpService, baseUrl, appConfig.getForkPoolThreshold(), appConfig.getBaseDir()));


        long endTime = System.currentTimeMillis();
        long duration = endTime-startTime;
        System.out.println("Download finished, took " + duration + "ms");
        System.out.println("Number of visited pages: " + visited.getVisited().size());
    }

//    private void recursiveFetchPages(String url) {
//        if (depth > appConfig.getMaxDepth()) {
//            System.out.println("Maximum recursion depth exceeded.");
//            return;
//        }
//        depth++;
//        System.out.println("Fetching link from: " + url);
//        String mainPage = httpService.fetchWebPage(url);
//        visited.visit(url);
//
//        List<String> links = getLinksFromHtml(mainPage, url);
//
//        links.forEach(link -> {
//            if (!visited.getVisited().contains(link)) {
//                recursiveFetchPages(link);
//            }
//        });
//    }
//
//    private List getLinksFromHtml(String html, String currentUrl) {
//        Document doc = Jsoup.parse(html);
//        Elements elements = doc.select("a[href]").select("a[href^=/]");
//
//        List<String> matches = elements.eachAttr("href");
//
//        List<String> links = new ArrayList<>();
//
//        matches.forEach(match -> {
//            // Remove anchors
//            if (match.indexOf("#") != -1) {
//                match = match.substring(0, match.indexOf("#"));
//            }
//            String link = baseUrl + match;
//            // Remove duplicates and base url
//            if (link != baseUrl && link != baseUrl + "/" && link != currentUrl && !links.contains(link)) {
//                links.add(link);
//            }
//        });
//
//        return links;
//    }
}
