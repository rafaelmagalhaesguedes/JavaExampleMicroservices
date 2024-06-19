package org.example;

import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

public class Server {
  private static final int PORT = 8081;
  private HttpServer server1;

  public Server() {
    try {
      server1 = HttpServer.create(new InetSocketAddress(PORT), 0);
      server1.createContext("/sales", new Service());
      server1.setExecutor(Executors.newFixedThreadPool(10));
    } catch (IOException e) {
      System.out.print("\nServer creation failed: " + e.getMessage());
    }
  }

  public void start() {
    server1.start();
    System.out.print("\nStore 1 Service started at the PORT " + PORT);
  }
}
