package com.otus.galaxy13.web.server;

import com.otus.galaxy13.web.server.application.exceptions.HTMLParseException;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;

public class HTMLParser {
    public static String parseHTML(String htmlTemplatePath, Object object) throws IOException, HTMLParseException {
        Field[] fields = object.getClass().getDeclaredFields();
        String html = Files.readString(Path.of(htmlTemplatePath), Charset.defaultCharset());
        for (Field field : fields) {
            String fieldName = field.getName();
            String htmlFieldName = String.format("${%s.%s}", object.getClass().getName().toLowerCase(Locale.ROOT), field.getName());
            if (html.contains(htmlFieldName)){
                try {
                    Method getterMethod = object.getClass().getMethod("get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1));
                    html = html.replaceAll(htmlFieldName, (String) getterMethod.invoke(object));
                } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                    throw new HTMLParseException(e);
                }
            } else {
                throw new HTMLParseException(new Exception("Field " + fieldName + " not found"));
            }
        }
        return html;
    }
}
