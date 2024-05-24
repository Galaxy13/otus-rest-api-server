package com.otus.galaxy13.web.server.application;

import com.otus.galaxy13.web.server.application.ddo.Item;
import com.otus.galaxy13.web.server.application.exceptions.ItemNotExists;
import com.otus.galaxy13.web.server.application.exceptions.NoDBConfigException;
import com.otus.galaxy13.web.server.application.properties.DBConnectionProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Storage {
    private static final Logger logger = LoggerFactory.getLogger(Storage.class);
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

    @SuppressWarnings("SameParameterValue")
    private static <T> T objFromResultSet(Class<T> objClass, ResultSet rs) throws SQLException, ClassCastException{
        Field[] fields = objClass.getDeclaredFields();
        T obj;
        try {
            obj = objClass.getConstructor().newInstance();
            for (Field field: fields){
                String fieldName = field.getName();
                Method method = objClass.getMethod("set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1), field.getType());
                Class<?> fieldType = field.getType();
                method.invoke(obj, rs.getObject(fieldName, fieldType));
            }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e){
            throw new ClassCastException(e.getMessage());
        }
        return obj;
    }

    private static String createGetAllItemsQuery(){
        return """
                select * from items""";
    }

    private static String createGetItemQuery(){
        return """
                select * from items where uuid = ?""";
    }

    private static String createNewItemQuery(){
        return """
                insert into items (uuid, title, price) values (?, ?, ?) returning uuid, title, price""";
    }

    private static String createUpdateItemQuery() {
        return """
                update items set title = ?, price = ? where uuid = ? returning uuid""";
    }

    private static String createDeleteItemQuery() {
        return """
                delete from items where uuid = ? returning *""";
    }

    public static List<Item> getItems() throws ClassNotFoundException, SQLException{
        try(Connection connection = createConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(createGetAllItemsQuery())){
            ResultSet rs = preparedStatement.executeQuery();
            List<Item> items = new ArrayList<>();
            try {
                while(rs.next()){
                    int price = rs.getInt("price");
                    UUID uuid = rs.getObject("uuid", UUID.class);
                    String title = rs.getString("title");
                    items.add(new Item(title, price, uuid));
                }
            } catch (SQLException e){
                logger.warn("SQL exception thrown from database.", e);
                throw e;
            }
            return items;
        }
    }

    public static Item getItem(UUID id) throws ClassNotFoundException, SQLException, ClassCastException, ItemNotExists {
        try(Connection connection = createConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(createGetItemQuery())) {
            preparedStatement.setObject(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()){
                try {
                    return objFromResultSet(Item.class, rs);
                } catch (ClassCastException e){
                    logger.warn("Error while cast received ResultSet to Item class. Make sure that SQL query is correct.");
                    throw e;
                } catch (SQLException e){
                    logger.warn("SQL exception thrown from database.", e);
                    throw e;
                }
            } else {
                throw new ItemNotExists();
            }
        }
    }

    public static Item save(Item item) throws SQLException, ClassNotFoundException {
        logger.trace(String.format("Item %s save requested", item.getTitle()));
        try(Connection connection = createConnection();
            PreparedStatement statement = connection.prepareStatement(createNewItemQuery())) {
            statement.setObject(1, item.getUuid());
            statement.setString(2, item.getTitle());
            statement.setInt(3, item.getPrice());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()){
               return objFromResultSet(Item.class, resultSet);
            } else {
                throw new SQLException("Unexpected exception while inserting new item in DB");
            }
        }
    }

    public static void update(Item item) throws SQLException, ClassNotFoundException, ItemNotExists {
        try (Connection connection = createConnection();
             PreparedStatement statement = connection.prepareStatement(createUpdateItemQuery())) {
            statement.setString(1, item.getTitle());
            statement.setInt(2, item.getPrice());
            statement.setObject(3, item.getUuid());
            statement.executeQuery();
        } catch (SQLException e) {
            if (e.getSQLState().equals("23505")) {
                logger.debug("Database throws exception 23505. Item not exists");
                throw new ItemNotExists();
            }
            logger.warn("Unexpected SQL exception thrown by database", e);
            throw e;
        }
    }

    public static Item delete(UUID uuid) throws SQLException, ClassNotFoundException, ItemNotExists {
        try (Connection connection = createConnection();
             PreparedStatement statement = connection.prepareStatement(createDeleteItemQuery())) {
            statement.setObject(1, uuid);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return objFromResultSet(Item.class, resultSet);
            } else {
                throw new ItemNotExists();
            }
        }
    }
}
