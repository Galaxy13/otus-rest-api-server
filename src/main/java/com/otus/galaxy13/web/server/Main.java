package com.otus.galaxy13.web.server;

import com.otus.galaxy13.web.server.http.HTTPServer;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    // Домашнее задание:
    // - Добавить логирование (с правильным выбором уровня логирования для сообщений)
    // - Сделайте так, чтобы Request по методу понимал имеет ли смысл вообще искать body в запросе (в GET запросе body не должно быть)
    // - * При получении PUT /products обновите данные продукта
    // PUT:
    // {
    //   "id": "4b798830-d2ad-4ee1-b4b9-03866cb75596",
    //   "title": "new-name",
    //   "price": 1
    // }
    // У продукта с id = 4b798830-d2ad-4ee1-b4b9-03866cb75596 поля должны быть изменены на те значения, что пришли в теле PUT запроса

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        int port = parsePortNumber(args);
        new HTTPServer(port).start();
    }

    private static int parsePortNumber(String[] args) {
        Options options = new Options();
        options.addOption("p", "port", true, "Server listen port");

        CommandLineParser parser = new DefaultParser();
        int defaultPort = 8189;

        try {
            CommandLine cmd = parser.parse(options, args);

            if (cmd.hasOption("p")) {
                return Integer.parseInt(cmd.getOptionValue("p"));
            } else {
                logger.warn("Port parameter is not provided. Server port set to default 8189");
                return defaultPort;
            }
        } catch (ParseException | NumberFormatException e) {
            logger.warn("Invalid port number provided. Server port set to default: 8189");
            return defaultPort;
        }
    }
}