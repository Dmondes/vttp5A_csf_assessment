package vttp.batch5.csf.assessment.server.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vttp.batch5.csf.assessment.server.repositories.OrdersRepository;
import vttp.batch5.csf.assessment.server.repositories.RestaurantRepository;

@Service
public class RestaurantService {

  @Autowired
  private OrdersRepository orderRepo;

  @Autowired
  private RestaurantRepository resRepo;

  // TODO: Task 2.2
  // You may change the method's signature
  public List<String> getMenu() {
    return orderRepo.getMenu();
  }

  // TODO: Task 4
  public String generateOrderId() {
    // Generate a random 8-character alphanumeric string
    String alphaNumeric = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    StringBuilder stringbuilder = new StringBuilder(8);
    java.util.Random random = new java.util.Random();

    for (int i = 0; i < 8; i++) {
      stringbuilder.append(alphaNumeric.charAt(random.nextInt(alphaNumeric.length())));
    }

    return stringbuilder.toString();
  }

  public boolean validateUser(String username, String password) {
    return resRepo.validateUser(username, password);
  }

  public void saveOrderToMySQL(String orderId, String paymentId, double totalCost, String username) {
    resRepo.saveOrderToMySQL(orderId, paymentId, totalCost, username);
  }

  public void saveOrderToMongoDB(String orderId, String paymentId, String username, jakarta.json.JsonArray items) {
    orderRepo.saveOrderToMongoDB(orderId, paymentId, username, items);
  }
}
