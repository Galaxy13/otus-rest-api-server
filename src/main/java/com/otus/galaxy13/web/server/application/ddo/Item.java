package com.otus.galaxy13.web.server.application.ddo;

import com.otus.galaxy13.web.server.application.exceptions.MergeException;
import com.otus.galaxy13.web.server.http.exceptions.WrongParameterException;

import java.util.Map;
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

    public Item(String title, int price, UUID uuid) {
        this.uuid = uuid;
        this.title = title;
        this.price = price;
    }

    public Item() {
    }

    public Item(Map<String, String> initMap) throws NumberFormatException, WrongParameterException {
        if (initMap.containsKey("title") && initMap.containsKey("price")) {
            this.title = initMap.get("title");
            this.price = Integer.parseInt(initMap.get("price"));
            this.uuid = UUID.randomUUID();
        } else {
            throw new WrongParameterException();
        }
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public void setUuid() {
        uuid = UUID.randomUUID();
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

    public void merge(Item item) throws MergeException {
        if (!item.getUuid().equals(this.uuid)) {
            throw new MergeException("Item UUIDs are not equal");
        } else {
            String newTitle = item.getTitle();
            Integer newPrice = item.getPrice();
            if (newPrice != null) {
                this.price = newPrice;
            }
            if (newTitle != null) {
                this.title = newTitle;
            }
        }
    }
}
