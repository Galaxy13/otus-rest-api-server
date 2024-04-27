package ru.otus.flamexander.web.server.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Storage {
    private static final Logger logger = LoggerFactory.getLogger(Storage.class);
    private static Map<UUID, Item> items;

    public static void init() {
        items = new HashMap<>();

        for (int i = 0; i < 3; i++) {
            Item item = new Item("item " + i, 100 + (int) (Math.random() * 1000));
            items.put(item.getId(), item);
        }
        logger.trace("Default items have put in List");
    }

    public static List<Item> getItems() {
        logger.trace("List of items requested");
        return items.values().stream().toList();
    }

    public static void save(Item item) {
        logger.trace(String.format("Item %s save requested", item.getTitle()));
        UUID itemUUID = UUID.randomUUID();
        item.setId(itemUUID);
        items.put(itemUUID, item);
    }

    public static boolean contains(UUID uuid) {
        return items.containsKey(uuid);
    }

    public static void modifyItem(UUID uuid, Item updateItem) {
        logger.trace(String.format("Item %s modification started", uuid));
        Item item = items.get(uuid);
        if (updateItem.getPrice() != null) {
            item.setPrice(updateItem.getPrice());
            logger.trace(String.format("Item %s price updated", uuid));
        }
        if (updateItem.getTitle() != null) {
            item.setTitle(updateItem.getTitle());
            logger.trace(String.format("Item %s title updated", uuid));
        }
        logger.trace(String.format("Item %s modification finished", uuid));
    }

    public static Item getItem(UUID uuid) {
        return items.get(uuid);
    }
}
