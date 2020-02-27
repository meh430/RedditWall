package com.mehul.redditwall;

public class Recommendation {
    private String name;
    private String description;
    private String url;

    public Recommendation(String n, String d, String u) {
        name = n;
        description = d;
        url = u;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }
}
