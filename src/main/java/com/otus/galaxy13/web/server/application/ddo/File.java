package com.otus.galaxy13.web.server.application.ddo;

public class File {
    private final String content;

    public File(String content) {
        this.content = content;
    }

    @SuppressWarnings("unused")
    public String getContent() {
        return content;
    }
}
