package com.otus.galaxy13.web.server.application;

import java.util.UUID;

public class Item {
    private UUID uuid;
    private String title;
    private Integer price;

    public Item(String title, int price) {
        this.uuid = UUID.randomUUID();
        this.title = title;
        this.price = price;
    }

    public Item(String title, int price, UUID uuid){
        this.uuid = uuid;
        this.title = title;
        this.price = price;
    }

    public Item(){
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
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

    public void setPrice(Integer price) {
        this.price = price;
    }
}
