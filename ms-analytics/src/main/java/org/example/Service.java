package org.example;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

class Service implements HttpHandler {

  @Override
  public void handle(HttpExchange exchange) throws IOException {
    try {
      CompletableFuture<Double> store1 = CompletableFuture
          .supplyAsync(() -> fetchSalesValue("http://localhost:8081/sales"));

      CompletableFuture<Double> store2 = CompletableFuture
          .supplyAsync(() -> fetchSalesValue("http://localhost:8082/sales"));

      // Aguarda a conclusão dos futuros
      store1.join();
      store2.join();

      // Obtém os valores reais dos futuros
      double store1Value = store1.get();
      double store2Value = store2.get();

      System.out.println("Valor da loja 1: " + store1Value);
      System.out.println("Valor da loja 2: " + store2Value);

      // Calcula o valor total
      double totalValue = store1Value + store2Value;

      // Envia a resposta ao cliente HTTP
      String response = "Total Sales Value for All Stores: " + totalValue;
      sendResponse(exchange, response);
    } catch (Exception e) {
      System.out.println("\nError na analytics: " + e.getMessage());
    }
  }


  private Double fetchSalesValue(String urlString) {
    try {
      URL url = new URL(urlString);
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      connection.setRequestMethod("GET");

      int responseCode = connection.getResponseCode();
      if (responseCode == 200) {
        try (InputStream inputStream = connection.getInputStream()) {
          byte[] buffer = new byte[1024];
          int bytesRead;
          StringBuilder stringBuilder = new StringBuilder();
          while ((bytesRead = inputStream.read(buffer)) != -1) {
            stringBuilder.append(new String(buffer, 0, bytesRead));
          }
          return Double.parseDouble(stringBuilder.toString());
        }
      }
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }
    return 0.0;
  }

  private void sendResponse(HttpExchange exchange, String response) {
    try {
      exchange.sendResponseHeaders(200, response.getBytes().length);
      OutputStream os = exchange.getResponseBody();
      os.write(response.getBytes());
      os.close();
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }
  }
}
