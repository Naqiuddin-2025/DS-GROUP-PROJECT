/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Database;
import java.util.HashMap;
import orderr.Order;
import orderr.OrderItem;
import orderr.OrderProcessing;
/**
 *
 * @author Naqiuddin
 */
public class DatabaseManager {
    // HashMap to store Users, using their unique ID as the key
    private HashMap <String, User> userTable = new HashMap<>();
    private HashMap <Integer, Order> orderTable = new HashMap<>(); 
    
    
    // Method to add a user
    public void addUser (User user){
        userTable.put(user.getUserID(), user);
    }
    // Method to retrieve a user in O(1) time
    public User getUser (String userID){
        return userTable.get(userID);
    }
    
    public void addOrder (Order order) {
        orderTable.put(order.getOrderID(), order);
    }
    public Order getOrder (int orderID) {
        return orderTable.get(orderID);
    }
}
