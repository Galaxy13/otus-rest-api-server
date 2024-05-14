package com.otus.galaxy13.web.server.application;

import java.util.UUID;

public class Item {
    private UUID id;
    private String title;
    private Integer price;

    public Item(String title, int price) {
        this.id = UUID.randomUUID();
        this.title = title;
        this.price = price;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
