package com.otus.galaxy13.web.server;

import com.otus.galaxy13.web.server.application.exceptions.HTMLParseException;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HTMLParser {
    public static <T> String parseHTML(String htmlTemplatePath, T object) throws IOException, HTMLParseException {
        Field[] fields = object.getClass().getDeclaredFields();
        String html = Files.readString(Path.of("templates/" + htmlTemplatePath), Charset.defaultCharset());
        Map<String, Method> getMethods = getStringMethodMap(fields, object);
        try {
            for (String key: getMethods.keySet()){
                Method method = getMethods.get(key);
                Object value = method.invoke(object);
                html = html.replaceAll(String.format("\\$\\{%s\\}", key), String.valueOf(value));
            }
        } catch (InvocationTargetException | IllegalAccessException | ClassCastException e) {
            throw new HTMLParseException(e);
        }
        return html;
    }

    public static <T> String parseTableHTML(String htmlTemplatePath, List<T> objects) throws IOException, HTMLParseException{
        T marker = objects.getFirst();
        Field[] fields = marker.getClass().getDeclaredFields();
        String html = Files.readString(Paths.get("templates/" + htmlTemplatePath), Charset.defaultCharset());
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
                    htmlTableContent.append("<td>").append(method.invoke(obj)).append("</td>");
                }
                htmlTableContent.append("</tr>");
            }
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new HTMLParseException(e);
        }
        return html.replaceAll("%%.*%%", htmlTableContent.toString());
    }

    private static <T> Map<String, Method> getStringMethodMap(Field[] fields, T marker) throws HTMLParseException {
        Map<String, Method> getMethods = new HashMap<>();
        for (Field field: fields){
            String fieldName = field.getName();
            String htmlFieldName = String.format("%s.%s", marker.getClass().getSimpleName().toLowerCase(Locale.ROOT), field.getName());
            try {
                Method getterMethod = marker.getClass().getMethod("get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1));
                getMethods.put(htmlFieldName, getterMethod);
            } catch (NoSuchMethodException e){
                throw new HTMLParseException(e);
            }
        }
        return getMethods;
    }
}
