package com.driver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class OrderDeliveryPartnerRepository {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    DeliveryPartnerRepository deliveryPartnerRepository;

    HashMap<String, String> orderDeliveryPairDb = new HashMap<>();

    HashMap<String, List<String>> deliveryPartnerOrdersDb = new HashMap<>();

    public String addPair(String orderId, String partnerId) {
        if (orderRepository.orderDb.containsKey(orderId) && deliveryPartnerRepository.deliveryPartnerDb.containsKey(partnerId)) {

            orderDeliveryPairDb.put(orderId, partnerId);

            List<String> ordersToBeHandled = deliveryPartnerOrdersDb.getOrDefault(partnerId, new ArrayList<>());
            ordersToBeHandled.add(orderId);
            deliveryPartnerOrdersDb.put(partnerId, ordersToBeHandled);

            DeliveryPartner deliveryPartner = deliveryPartnerRepository.deliveryPartnerDb.get(partnerId);
            deliveryPartner.setNumberOfOrders(ordersToBeHandled.size());
            deliveryPartnerRepository.deliveryPartnerDb.put(partnerId, deliveryPartner);

            return "Paired";
        }

        return null;
    }

    public List<String> getAllDeliveryPartnerOrders(String partnerId) {
        if (deliveryPartnerOrdersDb.isEmpty()) return null;

        return deliveryPartnerOrdersDb.get(partnerId).stream().collect(Collectors.toList());
    }

    public List<String> getAssignedDeliverPartnerIds() {
        if (orderDeliveryPairDb.isEmpty()) return null;

        return orderDeliveryPairDb.keySet().stream().collect(Collectors.toList());
    }

    public void deletePartnerById(String partnerId) {
        if (deliveryPartnerOrdersDb.containsKey(partnerId)) {
            List<String> orders = getAllDeliveryPartnerOrders(partnerId);

            for (String order: orders) {
                orderDeliveryPairDb.remove(order);
            }

            deliveryPartnerOrdersDb.remove(partnerId);

            return;
        }
        deliveryPartnerRepository.deliveryPartnerDb.remove(partnerId);
    }
}
