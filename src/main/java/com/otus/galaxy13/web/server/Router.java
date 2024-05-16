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
            pathUri = pathUri.substring(0, pathUri.trim().lastIndexOf("/"));
            Class<?> parameterType;
            if (parameter.equals("%s")){
                parameterType = String.class;
            } else if (parameter.equals("%d")){
                parameterType = Integer.class;
            } else throw new WrongParameterException(parameter);
            if (mainRouteMap.get(requestType).containsKey(pathUri)){
                RouterInstance pathRouter = mainRouteMap.get(requestType).get(pathUri);
                pathRouter.addParametrizedProcessor(parameterType, processor);
                return;
            } else {
                instance = new RouterInstance(pathUri);
                instance.addParametrizedProcessor(parameterType, processor);
            }
        }
        else {
            instance = new RouterInstance(pathUri, processor);
        }
        mainRouteMap.get(requestType).put(pathUri, instance);
    }

    public void get(String pathUri, Processor requestProcessor) throws WrongParameterException{
        putRouterInstance("GET", pathUri, requestProcessor);
    }

    public void post(String pathUri, Processor requestProcessor) throws WrongParameterException{
        putRouterInstance("POST", pathUri, requestProcessor);
    }

    public void put(String pathUri, Processor requestProcessor) throws WrongParameterException{
        putRouterInstance("PUT", pathUri, requestProcessor);
    }

    public void delete(String pathUri, Processor requestProcessor) throws WrongParameterException{
        putRouterInstance("DELETE", pathUri, requestProcessor);
    }

    public Processor getProcessor(HttpRequest request) throws HTTPError {
        Map<String, RouterInstance> instanceMap = mainRouteMap.get(request.getUri());
        if (instanceMap.containsKey(request.getUri())){
            return instanceMap.get(request.getUri()).getPathProcessor();
        } else {
            String strippedUri = request.getUri().substring(0, request.getUri().lastIndexOf("/"));
            if (instanceMap.containsKey(strippedUri)){
                return instanceMap.get(strippedUri).getParameterizedProcessor();
            } else {
                throw new HTTPError(404, request.getUri(), "Not Found", "Operaion not found, obsolete or not implemented");
            }
        }
    }
}
