package com.otus.galaxy13.web.server.application;

import com.otus.galaxy13.web.server.application.exceptions.NoDBConfigException;
import com.otus.galaxy13.web.server.application.properties.DBConnectionProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.*;

public class Storage {
    private final Logger logger = LoggerFactory.getLogger(Storage.class);
    private static DBConnectionProperties dbProperties;

    public static void init() throws NoDBConfigException {
        dbProperties = new DBConnectionProperties();
    }

    /**
     * Checks, if driver can be initialized and starts new connection
     *
     * @return new {@link Connection} to database
     * @throws ClassNotFoundException if Postgres driver failed to initialize
     * @throws SQLException           if SQL error occurs while opening connection
     */
    private static Connection createConnection() throws ClassNotFoundException, SQLException {
        Class.forName("org.postgresql.Driver");
        return DriverManager.getConnection(dbProperties.getJdbcURL(),
                dbProperties.getJdbcUsername(),
                dbProperties.getJdbcPassword());
    }

    private static String createGetAllItemsQuery(){
        return """
                select * from items""";
    }

    private static String createGetItemQuery(){
        return """
                select * from items where item_id = ?""";
    }

    private static String createNewItemQuery(){
        return """
                insert into items (uuid, title, price) values (?, ?, ?)""";
    }

    private static String createUpdateItemQuery(Map<String, String> params){
        StringJoiner stringJoiner = new StringJoiner(",", "update items set ", "where item_id = ?");
        params.forEach((key, value) -> stringJoiner.add(key + " = " + value));
        return stringJoiner.toString();
    }

    public static List<Item> getItems() throws ClassNotFoundException, SQLException{
        try(Connection connection = createConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(createGetAllItemsQuery())){
            ResultSet rs = preparedStatement.executeQuery();
            List<Item> items = new ArrayList<>();
            while(rs.next()){
                rs.getObject()
            }
        }
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

    public static Item getItem(int id){
        return items.get(id);
    }
}
