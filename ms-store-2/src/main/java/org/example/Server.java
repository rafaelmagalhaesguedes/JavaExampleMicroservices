package org.example;

import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

public class Server {
  private static final int PORT = 8082;
  private HttpServer server2;

  public Server() {
    try {
      server2 = HttpServer.create(new InetSocketAddress(PORT), 0);
      server2.createContext("/sales", new Service());
      server2.setExecutor(Executors.newFixedThreadPool(10));
    } catch (IOException e) {
      System.out.print("\nServer creation failed: " + e.getMessage());
    }
  }

  public void start() {
    server2.start();
    System.out.print("\nStore 2 Service started at the PORT " + PORT);
  }
}
