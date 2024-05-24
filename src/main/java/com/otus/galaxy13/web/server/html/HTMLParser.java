package com.otus.galaxy13.web.server.html;

import com.otus.galaxy13.web.server.application.processors.Processor;
import com.otus.galaxy13.web.server.html.exceptions.HTMLParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HTMLParser {
    private static final Logger logger = LoggerFactory.getLogger(Processor.class);

    public static <T> String parseHTML(String htmlTemplatePath, T object) throws IOException, HTMLParseException {
        logger.trace("HTML parsing started...");
        Field[] fields = object.getClass().getDeclaredFields();
        logger.trace("Fields retrieved from generic object");
        String html = Files.readString(Path.of(htmlTemplatePath), Charset.defaultCharset());
        logger.trace("HTML file read");
        Map<String, Method> getMethods = getStringMethodMap(fields, object);
        try {
            for (String key: getMethods.keySet()){
                Method method = getMethods.get(key);
                if (method == null) {
                    throw new HTMLParseException(new NullPointerException());
                }
                Object value = method.invoke(object);
                String pattern = String.format("\\$\\{%s\\}", key);
                if (Pattern.compile(pattern).matcher(html).find()) {
                    html = html.replaceAll(pattern, String.valueOf(value));
                    logger.trace("Field {} replaced", key);
                } else {
                    logger.warn("{} field not found. Check pattern template", key);
                }
            }
        } catch (InvocationTargetException | IllegalAccessException | ClassCastException e) {
            logger.warn("Object parsing failed", e);
            throw new HTMLParseException(e);
        }
        return html;
    }

    public static <T> String parseTableHTML(String htmlTemplatePath, List<T> objects) throws IOException, HTMLParseException{
        logger.trace("HTML table parsing started...");
        if (objects.isEmpty()) {
            return "";
        }
        T marker = objects.getFirst();
        Field[] fields = marker.getClass().getDeclaredFields();
        logger.trace("Fields retrieved from generic object");
        String html = Files.readString(Paths.get(htmlTemplatePath), Charset.defaultCharset());
        logger.trace("HTML template successfully read");
        Matcher matcher = Pattern.compile("%%(.+)%%").matcher(html);
        String[] tableFields;
        if (matcher.find()){
            tableFields = matcher.group(1).trim().split("\\s*,\\s*");
        } else {
            throw new HTMLParseException(new Exception());
        }
        Map<String, Method> getMethods = getStringMethodMap(fields, marker);
        StringBuilder htmlTableContent = new StringBuilder();
        try {
            for (T obj: objects){
                htmlTableContent.append("<tr>");
                for (String htmlTag: tableFields){
                    Method method = getMethods.get(htmlTag);
                    if (method == null) {
                        throw new HTMLParseException(new NullPointerException());
                    }
                    htmlTableContent.append("<td>").append(method.invoke(obj)).append("</td>");
                }
                logger.trace("Row parsed to string builder");
                htmlTableContent.append("</tr>");
            }
        } catch (InvocationTargetException | IllegalAccessException e) {
            logger.warn("HTML parsing failed", e);
            throw new HTMLParseException(e);
        }
        logger.debug("Table successfully parsed to HTML template");
        return html.replaceAll("%%.*%%", htmlTableContent.toString());
    }

    private static <T> Map<String, Method> getStringMethodMap(Field[] fields, T marker) throws HTMLParseException {
        logger.trace("Creating map of getter methods started...");
        Map<String, Method> getMethods = new HashMap<>();
        for (Field field: fields){
            String fieldName = field.getName();
            String htmlFieldName = String.format("%s.%s", marker.getClass().getSimpleName().toLowerCase(Locale.ROOT), field.getName());
            try {
                Method getterMethod = marker.getClass().getMethod("get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1));
                getMethods.put(htmlFieldName, getterMethod);
            } catch (NoSuchMethodException e){
                logger.error("No get method exists in parsed object class", e);
                throw new HTMLParseException(e);
            }
        }
        logger.debug("Map successfully created from object {}", marker);
        return getMethods;
    }
}
