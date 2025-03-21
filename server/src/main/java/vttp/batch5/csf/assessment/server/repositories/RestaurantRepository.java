package vttp.batch5.csf.assessment.server.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

// Use the following class for MySQL database
@Repository
public class RestaurantRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void saveOrderToMySQL(String orderId, String paymentId, double totalCost, String username) {
        String sql = "INSERT INTO place_orders (order_id, payment_id, order_date, total, username) VALUES (?, ?, CURDATE(), ?, ?)";
        jdbcTemplate.update(sql, orderId, paymentId, totalCost, username);
    }

    public boolean validateUser(String username, String password){
        return true;
      }
}
