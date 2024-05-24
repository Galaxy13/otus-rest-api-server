package com.otus.galaxy13.web.server.http;

import com.otus.galaxy13.web.server.application.Storage;
import com.otus.galaxy13.web.server.application.exceptions.NoDBConfigException;
import com.otus.galaxy13.web.server.http.ddo.HTTPRequest;
import com.otus.galaxy13.web.server.http.exceptions.BrokenHTTPRequestException;
import com.otus.galaxy13.web.server.http.routing.Dispatcher;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SuppressWarnings("InfiniteLoopStatement")
public class HTTPServer {
    private final int port;
    private Dispatcher dispatcher;
    private final org.slf4j.Logger logger = LoggerFactory.getLogger(HTTPServer.class);

    public HTTPServer(int port) {
        this.port = port;
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port);
             ExecutorService executorService = Executors.newFixedThreadPool(5)) {
            logger.info("Server started on port: " + port);
            this.dispatcher = new Dispatcher();
            logger.debug("Dispatcher initialized");
            Storage.init();
            logger.debug("Storage initialized");
            while (true){
                Socket socket = serverSocket.accept();
                logger.info("Received socket connection from " + socket.getLocalAddress().getHostAddress());
                    executorService.execute(() -> {
                        try (socket) {
                            byte[] buffer = new byte[8192];
                            int n = socket.getInputStream().read(buffer);
                            String rawRequest = new String(buffer, 0, n);
                            logger.debug("Socket message " + socket.getLocalAddress().getHostAddress() + " read");
                            try {
                                HTTPRequest request = new HTTPRequest(rawRequest);
                                logger.debug("HTTP request handled, HTTPRequest object created");
                                request.info();
                                dispatcher.execute(request, socket.getOutputStream());
                                logger.trace("Dispatcher handles request from " + socket.getLocalAddress().getHostAddress());
                                logger.info(String.format("HTTP %s request from %s handled", request.getRouteKey(), socket.getLocalAddress().getHostAddress()));
                            } catch (BrokenHTTPRequestException e){
                                logger.warn(e.getMessage() + String.format(" from %s", socket
                                        .getLocalAddress()
                                        .getHostAddress()));
                            }
                        } catch (IOException e){
                            logger.warn("Client socket error", e);
                        } catch (StringIndexOutOfBoundsException e){
                            logger.warn("Buffer read error, String is out of bounds", e);
                        }
                    });
                }
        } catch (IOException | NoDBConfigException e) {
            logger.error("No DB Config. Server start halted ", e);
        }
    }
}
