package vttp.batch5.csf.assessment.server.repositories;

import java.util.Date;
import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import jakarta.json.JsonArray;


@Repository
public class OrdersRepository {

  @Autowired
  private MongoTemplate mongoTemplate;

  // TODO: Task 2.2
  // You may change the method's signature
  // Write the native MongoDB query in the comment below
  //db.getCollection("menus").find({ "name": { $nin: ["", null] } },{}).sort({ "name": 1 })
  //  Native MongoDB query here
  //
  public List<String> getMenu() {
    Criteria criteria =  Criteria.where("name").nin("", null);
    Query query = Query.query(criteria).with(Sort.by(Sort.Direction.ASC, "name"));
    List<String> menu = mongoTemplate.find(query, String.class, "menus" );
		return menu;
    
  }

  // TODO: Task 4
  // Write the native MongoDB query for your access methods in the comment below
  //// db.orders.insertOne({
  //   _id: "<order_id>",
  //   payment_id: "<payment_id>",
  //   username: "<username>",
  //   total: <total_cost>,
  //   timestamp: new Date(),
  //   items: [<items_array>]
  //  Native MongoDB query here

    public void saveOrderToMongoDB(String orderId, String paymentId, String username, JsonArray items) {
    Document orderDoc = new Document();
    orderDoc.append("_id", orderId);
    orderDoc.append("payment_id", paymentId);
    orderDoc.append("username", username);
    orderDoc.append("timestamp", new Date());
    mongoTemplate.insert(orderDoc, "orders");
    }
  
}
