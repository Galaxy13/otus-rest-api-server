package com.otus.galaxy13.web.server.http.routing;

import com.otus.galaxy13.web.server.application.processors.Processor;
import com.otus.galaxy13.web.server.http.ddo.HTTPRequest;
import com.otus.galaxy13.web.server.http.ddo.Response;
import com.otus.galaxy13.web.server.http.exceptions.HTTPError;

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

    public void putRouterInstance(String requestType, String pathUri, Processor processor){
        Matcher matcher = Pattern.compile("(?<=/\\{)\\S+(?=}$)").matcher(pathUri);
        RouterInstance instance;
        if (matcher.find()){
            pathUri = pathUri.substring(0, pathUri.trim().lastIndexOf("/"));
            if (pathUri.isEmpty()){
                pathUri = "/";
            }
            if (mainRouteMap.get(requestType).containsKey(pathUri)){
                RouterInstance pathRouter = mainRouteMap.get(requestType).get(pathUri);
                pathRouter.addParametrizedProcessor(processor);
                return;
            } else {
                instance = new RouterInstance();
                instance.addParametrizedProcessor(processor);
            }
        }
        else {
            instance = new RouterInstance(processor);
        }
        mainRouteMap.get(requestType).put(pathUri, instance);
    }

    public void get(String pathUri, Processor requestProcessor){
        putRouterInstance("GET", pathUri, requestProcessor);
    }

    public void post(String pathUri, Processor requestProcessor){
        putRouterInstance("POST", pathUri, requestProcessor);
    }

    public void put(String pathUri, Processor requestProcessor){
        putRouterInstance("PUT", pathUri, requestProcessor);
    }

    public void delete(String pathUri, Processor requestProcessor){
        putRouterInstance("DELETE", pathUri, requestProcessor);
    }

    public Response parseRequest(HTTPRequest request) throws HTTPError, ClassNotFoundException {
        Map<String, RouterInstance> instanceMap = mainRouteMap.get(request.getMethod());
        if (instanceMap.containsKey(request.getUri())){
            return instanceMap.get(request.getUri()).getPathProcessor().execute(request);
        } else {
            int indexOfLastSlash = request.getUri().lastIndexOf("/");
            String strippedUri = request.getUri().substring(0, indexOfLastSlash);
            if (strippedUri.isEmpty()) {
                strippedUri = "/";
            }
            String parameter = request.getUri().substring(indexOfLastSlash + 1);
            if (instanceMap.containsKey(strippedUri)){
                RouterInstance routerInstance = instanceMap.get(strippedUri);
                return routerInstance.getParameterizedProcessor().execute(request, parameter);
            } else {
                throw new HTTPError(404, request.getUri(), "Not Found", "Operaion not found, obsolete or not implemented");
            }
        }
    }
}
