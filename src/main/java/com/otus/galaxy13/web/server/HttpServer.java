package com.otus.galaxy13.web.server;

import com.otus.galaxy13.web.server.application.exceptions.HTTPError;
import com.otus.galaxy13.web.server.application.processors.ResponseProcessor;
import org.slf4j.LoggerFactory;
import com.otus.galaxy13.web.server.application.Storage;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SuppressWarnings("InfiniteLoopStatement")
public class HttpServer {
    private final int port;
    private Dispatcher dispatcher;
    private final org.slf4j.Logger logger = LoggerFactory.getLogger(HttpServer.class);

    public HttpServer(int port) {
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
                                HttpRequest request = new HttpRequest(rawRequest);
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
        } catch (IOException e) {
            logger.error("Server internal error", e);
        }
    }
}
