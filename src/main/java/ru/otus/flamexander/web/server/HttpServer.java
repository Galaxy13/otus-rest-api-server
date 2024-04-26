package ru.otus.flamexander.web.server;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.LogManager;
import java.util.logging.Logger;

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
             ExecutorService executorService = Executors.newFixedThreadPool(5);
             InputStream stream = this.getClass().getClassLoader().getResourceAsStream("logging.properties")) {
            LogManager.getLogManager().readConfiguration(stream);
            Logger logger = Logger.getLogger(HttpServer.class.getName());
            logger.info("Сервер запущен на порту: " + port);
            this.dispatcher = new Dispatcher();
            logger.config("Диспетчер проинициализирован");
            while (true){
                Socket socket = serverSocket.accept();
                logger.fine("Принято соединение с сокета " + socket.getLocalAddress().getHostAddress());
                    executorService.execute(() -> {
                        try (socket) {
                            byte[] buffer = new byte[8192];
                            int n = socket.getInputStream().read(buffer);
                            String rawRequest = new String(buffer, 0, n);
                            logger.finest("Cooбщение от сокета " + socket.getLocalAddress().getHostAddress() + " прочитано");
                            try {
                                HttpRequest request = new HttpRequest(rawRequest);
                                logger.finer("HTTP запрос обработан, создан объект HTTPRequest");
                                request.info(debugDepth);
                                logger.entering("Dispatcher", "execute");
                                dispatcher.execute(request, socket.getOutputStream());
                                logger.finer("Диспетчер обработал запрос с " + socket.getLocalAddress().getHostAddress());
                            } catch (BrokenHTTPRequestException e){
                                logger.warning(e.getMessage() + String.format(" from %s", socket
                                        .getLocalAddress()
                                        .getHostAddress()));
                            }
                        } catch (IOException e){
                            logger.warning(e.getMessage());
                        } catch (StringIndexOutOfBoundsException e){
                            logger.warning("Buffer read error, String is out of bounds");
                        }
                    });
                }
        } catch (IOException e) {
            Logger.getAnonymousLogger().severe(e.getMessage());
        }
    }
}
