package com.driver;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class OrderDeliveryPartnerRepository {
    static HashMap<String, List<String>> orderDeliveryPairDb = new HashMap<>();

    static public String addPair(String orderId, String partnerId) {
        if (OrderRepository.orderDb.containsKey(orderId) && DeliveryPartnerRepository.deliveryPartnerDb.containsKey(partnerId)) {
            List<String> ordersToHandle = orderDeliveryPairDb.getOrDefault(partnerId, new ArrayList<>());

            DeliveryPartner deliveryPartner = DeliveryPartnerRepository.deliveryPartnerDb.get(partnerId);
            deliveryPartner.setNumberOfOrders(ordersToHandle.size());

            ordersToHandle.add(orderId);

            orderDeliveryPairDb.put(partnerId, ordersToHandle);

            return "Paired";
        }

        return null;
    }

    static public List<String> getAllDeliveryPartnerOrders(String partnerId) {
        if (orderDeliveryPairDb.isEmpty()) return null;

        return orderDeliveryPairDb.get(partnerId).stream().collect(Collectors.toList());
    }

    static List<String> getAssignedDeliverPartnerIds() {
        if (orderDeliveryPairDb.isEmpty()) return null;

        return orderDeliveryPairDb.keySet().stream().collect(Collectors.toList());
    }

    static public void deletePartnerById(String partnerId) {
        if (orderDeliveryPairDb.isEmpty() || !orderDeliveryPairDb.containsKey(partnerId)) {
            return;
        }
        orderDeliveryPairDb.remove(partnerId);
    }
}
