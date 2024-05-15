package com.otus.galaxy13.web.server.application.properties;

import com.otus.galaxy13.web.server.application.exceptions.NoDBConfigException;

public class DBConnectionProperties {
    private final String jdbcURL;

    private final String jdbcUsername;

    private final String jdbcPassword;

    public DBConnectionProperties() throws NoDBConfigException {
        // Example envs
        // DB_USER=postgres;
        // DB_PASSWD=1234
        // DB_URL=jdbc:postgresql://localhost:5432/
        String url = System.getenv("DB_URL");
        String username = System.getenv("DB_USER");
        String password = System.getenv("DB_PASSWD");
        if (username != null) {
            jdbcUsername = username;
        } else {
            throw new NoDBConfigException();
        }
        if (password != null) {
            jdbcPassword = password;
        } else {
            throw new NoDBConfigException();
        }
        if (url != null) {
            jdbcURL = url;
        } else {
            throw new NoDBConfigException();
        }
    }

    public String getJdbcPassword() {
        return jdbcPassword;
    }

    public String getJdbcUsername() {
        return jdbcUsername;
    }


    public String getJdbcURL() {
        return jdbcURL;
    }

}
