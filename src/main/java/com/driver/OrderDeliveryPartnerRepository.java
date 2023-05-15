package com.driver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class OrderDeliveryPartnerRepository {

//    @Autowired
    OrderRepository orderRepository = new OrderRepository();

    HashMap<String, List<String>> orderDeliveryPairDb = new HashMap<>();

    public String addPair(String orderId, String partnerId) {
        if (orderRepository.orderDb.containsKey(orderId) && DeliveryPartnerRepository.deliveryPartnerDb.containsKey(partnerId)) {
            List<String> ordersToHandle = orderDeliveryPairDb.getOrDefault(partnerId, new ArrayList<>());

            DeliveryPartner deliveryPartner = DeliveryPartnerRepository.deliveryPartnerDb.get(partnerId);
            deliveryPartner.setNumberOfOrders(ordersToHandle.size() + 1);

            ordersToHandle.add(orderId);

            orderDeliveryPairDb.put(partnerId, ordersToHandle);

            return "Paired";
        }

        return null;
    }

    public List<String> getAllDeliveryPartnerOrders(String partnerId) {
        if (orderDeliveryPairDb.isEmpty()) return null;

        return orderDeliveryPairDb.get(partnerId).stream().collect(Collectors.toList());
    }

    public List<String> getAssignedDeliverPartnerIds() {
        if (orderDeliveryPairDb.isEmpty()) return null;

        return orderDeliveryPairDb.keySet().stream().collect(Collectors.toList());
    }

    public void deletePartnerById(String partnerId) {
        if (orderDeliveryPairDb.isEmpty() || !orderDeliveryPairDb.containsKey(partnerId)) {
            return;
        }
        orderDeliveryPairDb.remove(partnerId);
    }
}
