package vttp.batch5.csf.assessment.server.controllers;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import java.io.StringReader;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vttp.batch5.csf.assessment.server.services.RestaurantService;

@RestController
@RequestMapping("/api")
public class RestaurantController {

    @Autowired
    private RestaurantService restService;

    // TODO: Task 2.2
    // You may change the method's signature
    @GetMapping(path = "/menu", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getMenus() {
        List<String> menuList = restService.getMenu();
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        for (String item : menuList) {
            item = item.replace("_", "");
            arrayBuilder.add(item);
        }
        JsonArray allMenu = arrayBuilder.build();
        // System.out.println(allMenu.toString());
        return ResponseEntity.ok(allMenu.toString());
    }

    // TODO: Task 4
    // Do not change the method's signature
    @PostMapping(
        path = "/food_order",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<String> postFoodOrder(@RequestBody String payload) {
        try {
            JsonReader reader = Json.createReader(new StringReader(payload));
            JsonObject orderData = reader.readObject();
            String username = orderData.getString("username");
            String password = orderData.getString("password");
            JsonArray items = orderData.getJsonArray("items");
            String paymentId = "";
             double totalCost = orderData.getJsonNumber("totalCost").doubleValue();
            String orderId = restService.generateOrderId();
            boolean isValid = restService.validateUser(username, password);
            if (!isValid) {
                JsonObject errorResponse = Json.createObjectBuilder()
                    .add("message", "error")
                    .build();
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    errorResponse.toString()
                );
            }
            restService.saveOrderToMySQL(
                orderId,
                paymentId,
                totalCost,
                username
            );
            restService.saveOrderToMongoDB(orderId, paymentId, username, items);
            return ResponseEntity.ok("success");
        } catch (Exception e) {
            JsonObject errorResponse = Json.createObjectBuilder()
                .add("message", "Invalid username and/or password")
                .build();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                errorResponse.toString()
            );
        }
    }
}
