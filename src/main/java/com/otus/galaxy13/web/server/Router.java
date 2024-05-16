package com.otus.galaxy13.web.server;

import com.otus.galaxy13.web.server.application.exceptions.HTTPError;
import com.otus.galaxy13.web.server.application.exceptions.WrongParameterException;
import com.otus.galaxy13.web.server.application.processors.Processor;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Router {
    Map<String, Map<String, RouterInstance>> mainRouteMap = new HashMap<>(){{
        put("GET", new HashMap<>());
        put("POST", new HashMap<>());
        put("PUT", new HashMap<>());
        put("DELETE", new HashMap<>());
    }};

    public void putRouterInstance(String requestType, String pathUri, Processor processor) throws WrongParameterException{
        Matcher matcher = Pattern.compile("(?<=/\\{)%s|%d(?=})").matcher(requestType);
        RouterInstance instance;
        if (matcher.find()){
            String parameter = matcher.group(1);
            requestType = pathUri.substring(0, pathUri.trim().lastIndexOf("/"));
            if (parameter.equals("s")){
                instance = new RouterInstance(requestType, processor, String.class);
            } else if (parameter.equals("d")){
                instance = new RouterInstance(requestType, processor, Integer.class);
            } else throw new WrongParameterException(parameter);
        }
        else {
            instance = new RouterInstance(requestType, processor);
        }
        mainRouteMap.get(requestType).put(pathUri, instance);
    }

    public RouterInstance get(String pathUri, Processor requestProcessor){
        return putRouterInstance("GET", pathUri, requestProcessor);
    }

    public RouterInstance post(String pathUri, Processor requestProcessor){
        return putRouterInstance("POST", pathUri, requestProcessor);
    }

    public RouterInstance put(String pathUri, Processor requestProcessor){
        return putRouterInstance("PUT", pathUri, requestProcessor);
    }

    public RouterInstance delete(String pathUri, Processor requestProcessor){
        return putRouterInstance("DELETE", pathUri, requestProcessor);
    }

    public Processor getProcessor(HttpRequest request) throws HTTPError {

    }
}
