package org.example;

import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

public class Server {
  private static final int PORT = 8080;
  private HttpServer server;

  public Server() {
    try {
      server = HttpServer.create(new InetSocketAddress(PORT), 0);
      server.createContext("/", new Service());
      server.setExecutor(Executors.newFixedThreadPool(10));
    } catch (IOException e) {
      System.out.println("Falha na criação do servidor: " + e.getMessage());
    }
  }

  public void start() {
    server.start();
    System.out.println("Aggregator Service iniciado na porta " + PORT);
  }
}
