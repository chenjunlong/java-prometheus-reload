package com.github.jpreload;

import com.github.jpreload.config.Config;
import com.github.jpreload.utils.NetUtils;
import com.sun.net.httpserver.HttpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by chenjunlong on 2018/8/1.
 */
public class Bootstrap {

    private static Logger logger = LoggerFactory.getLogger(Bootstrap.class);

    private static String HOST = NetUtils.getLocalHost();
    private static int PORT = Integer.parseInt(Config.PORT);

    public static void main(String[] args) throws IOException {
        InetSocketAddress addr = new InetSocketAddress(HOST, PORT);
        HttpServer server = HttpServer.create();
        server.bind(addr, 20);
        server.createContext("/register", new ReloadHandler());
        ExecutorService executorService = Executors.newFixedThreadPool(20, Executors.defaultThreadFactory());
        server.setExecutor(executorService);
        server.start();

        ExecutorService reloadTaskExecutorService = Executors.newFixedThreadPool(1, Executors.defaultThreadFactory());
        reloadTaskExecutorService.execute(new ReloadTask());
        logger.info("java-prometheus-reload started ...");
    }
}
