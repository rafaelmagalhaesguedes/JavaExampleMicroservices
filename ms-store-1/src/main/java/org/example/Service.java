package org.example;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

class Service implements HttpHandler {
  @Override
  public void handle(HttpExchange exchange) throws IOException {
    double totalSales = calculateTotalSales();
    String response = "Total sales Store 1\n: " + totalSales;
    sendResponse(exchange, response);
  }

  private double calculateTotalSales() {
    double totalSales = 0.0;
    try {
      File xmlFile = new File("Store1.xml");
      DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
      Document doc = dBuilder.parse(xmlFile);
      doc.getDocumentElement().normalize();

      NodeList nList = doc.getElementsByTagName("sale");

      for (int temp = 0; temp < nList.getLength(); temp++) {
        int quantidade = Integer.parseInt(doc.getElementsByTagName("quantity").item(temp).getTextContent());
        double valor = Double.parseDouble(doc.getElementsByTagName("value").item(temp).getTextContent());
        totalSales += quantidade * valor;
      }
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    return totalSales;
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
