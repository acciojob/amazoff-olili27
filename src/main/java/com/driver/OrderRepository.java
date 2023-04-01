package com.driver;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class OrderRepository {
    static HashMap<String, Order>  orderDb = new HashMap<>();

    static public void addOrder(Order order) {
        String key = order.getId();

        orderDb.put(key, order);
    }

    static public List<Order> getAllOrders() {
        if (orderDb.isEmpty()) return null;

        return orderDb.values().stream().collect(Collectors.toList());
    }

    static public void deleteOrderById(String orderId) {
        if (orderDb.isEmpty() || !orderDb.containsKey(orderId)) {
            return;
        }
        orderDb.remove(orderId);
    }
}
