package com.example.SiteDownloader.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;

/**
 * Implements a RecursiveAction to fetch a web page, save it to the system, and traverses the links on the page.
 * The folder structure will imitate the pages url structure.
 */
public class SiteDownloaderRecursiveTask extends RecursiveAction {
    private int threshold;
    private String url;
    private String baseUrl;
    private VisitedSites visited;
    private HttpService httpService;
    private String baseDir = "out/";

    public SiteDownloaderRecursiveTask(String url, VisitedSites visited, HttpService httpService, String baseUrl, int threshold, String baseDir) {
        this.threshold = threshold;
        this.url = url;
        this.visited = visited;
        this.httpService = httpService;
        this.baseUrl = baseUrl;
        this.baseDir = baseDir;
    }

    /**
     * Initiate a recursive actions to fetch and save a web page and add new actions for all the links on the page.
     */
    @Override
    protected void compute() {
        if (visited.getVisited().size() > threshold) {
            // Max threshold reached, end task
            return;
        } else {
            System.out.println("Fetching link from: " + url);
            // Fetch web page and extract all of its links
            String mainPage = httpService.fetchWebPage(url);
            List<String> links = getLinksFromHtml(mainPage, url);

            // Save HTML to file
            createFile(mainPage);

            List<SiteDownloaderRecursiveTask> subtasks = new ArrayList<>();

            // Create a new task for each of the links
            links.forEach(link -> {
                // Always remove trailing slash from links
                if (link.endsWith("/")) {
                    try {
                        link = link.substring(0, link.lastIndexOf("/"));
                    } catch (StringIndexOutOfBoundsException e) {
                        e.printStackTrace();
                    }
                }
                // Add a new subtask for the links, unless they have already been visited
                if (!visited.getVisited().contains(link)) {
                    visited.visit(link);
                    subtasks.add(new SiteDownloaderRecursiveTask(link, visited, httpService, baseUrl, threshold, baseDir));
                }
            });
            // Invoke all subtasks
            ForkJoinTask.invokeAll(subtasks);
        }
    }

    /**
     * Takes a web page and saves it to a directory corresponding to the pages path
     *
     * @param page - The page to save
     */
    protected void createFile(String page) {
        String dirName = baseDir + url;
        String fileName = dirName.substring(dirName.lastIndexOf("/") + 1) + ".html";
        dirName = dirName.substring(0, dirName.lastIndexOf("/"));
        try {
            // Create directories from path
            Path dir = Paths.get(dirName);
            Files.createDirectories(dir);

            // Create the file in the right directory
            Path filePath = Paths.get(dirName + fileName);
            if (!Files.exists(filePath)) {
                PrintWriter writer = new PrintWriter(Files.newBufferedWriter(dir.resolve(fileName)));
                writer.write(page);
            }
        } catch (IOException e) {
            System.out.println("Failed to create file: " + url);
            e.printStackTrace();
        }
    }

    /**
     * Fetches all links from a html page and returns a list of the urls
     *
     * @param html       - Html to search
     * @param currentUrl - Url to the current page
     * @return
     */
    protected List getLinksFromHtml(String html, String currentUrl) {
        Document doc = Jsoup.parse(html);
        Elements elements = doc.select("a[href]").select("a[href^=/]");

        List<String> matches = elements.eachAttr("href");

        List<String> links = new ArrayList<>();

        matches.forEach(match -> {
            // Remove anchors
            if (match.indexOf("#") != -1) {
                match = match.substring(0, match.indexOf("#"));
            }
            // Remove query params
            if (match.indexOf("?") != -1) {
                match = match.substring(0, match.indexOf("?"));
            }
            String link = baseUrl + match;
            // Remove duplicates and base url
            if (link != baseUrl && link != baseUrl + "/" && link != currentUrl && !links.contains(link)) {
                links.add(link);
            }
        });

        return links;
    }
}
