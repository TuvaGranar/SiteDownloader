package com.example.SiteDownloader.service;

import java.util.ArrayList;
import java.util.List;

public class VisitedSites {
    List<String> visited = new ArrayList<>();

    public List<String> getVisited() {
        return this.visited;
    }

    public void visit(String site) {
        visited.add(site);
    }
}
