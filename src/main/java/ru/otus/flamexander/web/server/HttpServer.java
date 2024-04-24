package ru.otus.flamexander.web.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SuppressWarnings("InfiniteLoopStatement")
public class HttpServer {
    private final int port;
    private Dispatcher dispatcher;
    private final int debugDepth;

    public HttpServer(int port, int debugDepth) {
        this.port = port;
        this.debugDepth = (debugDepth > 2 || debugDepth < 0) ? 0: debugDepth;
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port);
             ExecutorService executorService = Executors.newFixedThreadPool(5)) {
            System.out.println("Сервер запущен на порту: " + port);
            this.dispatcher = new Dispatcher();
            System.out.println("Диспетчер проинициализирован");
            while (true){
                Socket socket = serverSocket.accept();
                    executorService.execute(() -> {
                        try (socket) {
                            byte[] buffer = new byte[8192];
                            int n = socket.getInputStream().read(buffer);
                            String rawRequest = new String(buffer, 0, n);
                            try {
                                HttpRequest request = new HttpRequest(rawRequest);
                                request.info(debugDepth);
                                dispatcher.execute(request, socket.getOutputStream());
                            } catch (BrokenHTTPRequestException e){
                                System.out.println(e.getMessage()
                                        + String.format(" from %s", socket
                                        .getLocalAddress()
                                        .getHostAddress()));
                            }
                        } catch (IOException e){
                            e.printStackTrace(System.out);
                        }
                    });
                }
        } catch (IOException e) {
            e.printStackTrace(System.out);
        }
    }
}
