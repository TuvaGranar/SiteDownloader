package com.example.SiteDownloader.service;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ForkJoinPool;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
public class SiteDownloaderRecursiveTaskTests {

    private final String testPageWithLinks = "src/test/resources/testPageWithLinks.html";
    private final String urlWithLinks = "/testpagewithlinks";
    private final String testPageNoLinks = "src/test/resources/testPageNoLinks.html";
    private final String urlNoLinks = "/testpagenolinks";

    private final String testPageRelative = "src/test/resources/relativelink.html";
    private final String urlRelative = "/testpagewithlinks/relativelink";

    private final String baseDir = "src/test/out/";

    private String htmlWithLinks;
    private String htmlRelativeLink;
    private String htmlNoLinks;

    @Mock
    private HttpService httpService;

    @BeforeAll
    public void init() throws IOException {
        htmlWithLinks = readFile(testPageWithLinks);
        htmlRelativeLink = readFile(testPageRelative);
        htmlNoLinks = readFile(testPageNoLinks);

        Mockito.when(httpService.fetchWebPage(ArgumentMatchers.eq(urlWithLinks))).thenReturn(htmlWithLinks);
        Mockito.when(httpService.fetchWebPage(ArgumentMatchers.eq(urlRelative))).thenReturn(htmlRelativeLink);
        Mockito.when(httpService.fetchWebPage(ArgumentMatchers.eq(urlNoLinks))).thenReturn(htmlNoLinks);
    }

    @Test
    public void testSiteDownloaderRecursiveTask_NoLinks() {
        SiteDownloaderRecursiveTask siteDownloaderRecursiveTask = new SiteDownloaderRecursiveTask(urlNoLinks, new VisitedSites(), httpService, urlNoLinks, 10, baseDir);

        new ForkJoinPool().invoke(siteDownloaderRecursiveTask);

        Path path = Paths.get(baseDir + urlNoLinks + ".html");
        Assert.assertTrue(Files.exists(path));
    }

    @Test
    public void testSiteDownloaderRecursiveTask_WithLinks() {
        SiteDownloaderRecursiveTask siteDownloaderRecursiveTask = new SiteDownloaderRecursiveTask(urlWithLinks, new VisitedSites(), httpService, urlWithLinks, 10, baseDir);

        new ForkJoinPool().invoke(siteDownloaderRecursiveTask);

        Path path1 = Paths.get(baseDir + urlWithLinks + ".html");
        Path path2 = Paths.get(baseDir + urlRelative + ".html");
        Assert.assertTrue(Files.exists(path1));
        Assert.assertTrue(Files.exists(path2));
    }

    private String readFile(String file) throws IOException {
        Path path = Paths.get(file);
        return Files.readString(path);
    }

}
