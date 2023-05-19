package com.driver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderDeliveryPartnerRepository orderDeliveryPartnerRepository;

    @Autowired
    DeliveryPartnerRepository deliveryPartnerRepository;

    public void addOrder(Order order) {
        orderRepository.addOrder(order);
    }
    public void addPartner(String partnerId) {
        deliveryPartnerRepository.addPartner(partnerId);
    }

    public String addPair(String orderId, String partnerId) {
        return orderDeliveryPartnerRepository.addPair(orderId, partnerId);
    }

    public Order getOrderById(String orderId) {
        List<Order> orders = orderRepository.getAllOrders();

        if (orders != null) {
            for (Order order : orders) {
                if (order.getId().equals(orderId)) {
                    return order;
                }
            }
        }

        return null;
    }

    public DeliveryPartner getDeliveryPartnerById(String partnerId) {
        List<DeliveryPartner> deliverPartners = deliveryPartnerRepository.getAllDeliveryPartners();

        if (deliverPartners != null) {
            for (DeliveryPartner deliveryPartner : deliverPartners) {
                if (deliveryPartner.getId().equals(partnerId)) {
                    return deliveryPartner;
                }
            }
        }

        return null;
    }

    public List<String> getAllDeliveryPartnerOrders(String partnerId) {
        return orderDeliveryPartnerRepository.getAllDeliveryPartnerOrders(partnerId);
    }

    public List<String> getAllOrders() {
        List<Order> orders = orderRepository.getAllOrders();
        List<String> orderIds = new ArrayList<>();

        if(orders != null){
            for (Order order : orders) {
                orderIds.add(order.getId());
            }

            return orderIds;
        }

        return null;
    }

    public int numberOfAssignedOrders() {
        List<String> assignedPartnerIds = orderDeliveryPartnerRepository.getAssignedDeliverPartnerIds();

        int countAssignedOrders = 0;

        if(assignedPartnerIds != null) {
            for (String partnersId: assignedPartnerIds) {
                DeliveryPartner deliveryPartner = deliveryPartnerRepository.deliveryPartnerDb.get(partnersId);

                countAssignedOrders += deliveryPartner.getNumberOfOrders();
            }
        }

      return countAssignedOrders;
    }

    public int numberOfUnAssignedOrders() {
       return orderRepository.orderDb.size() - orderDeliveryPartnerRepository.orderDeliveryPairDb.size();
    }

    public int getOrdersLeftAfterGivenTimeByPartnerId(String time, String partnerId) {
        List<String> orderIds = orderDeliveryPartnerRepository.getAllDeliveryPartnerOrders(partnerId);

        int countLeft = 0;

        if (orderIds != null) {
            for (String orderId: orderIds) {
                Order order = orderRepository.orderDb.get(orderId);

                if(order.getDeliveryTime() > ( Integer.parseInt(time.substring(0,2)) + Integer.parseInt(time.substring(3)))) {
                    countLeft++;
                }
            }
        }

        return countLeft;
    }

    public String getLastDeliveryTimeByPartnerId(String partnerId) {
        List<String> orderIds = orderDeliveryPartnerRepository.getAllDeliveryPartnerOrders(partnerId);

        List<Integer> deliveryTimes = new ArrayList<>();

        if (orderIds != null) {
            for (String orderId: orderIds) {
                Order order = orderRepository.orderDb.get(orderId);
                deliveryTimes.add(order.getDeliveryTime());
            }

            deliveryTimes.sort((a, b) -> {
                return b - a;
            });


            int deliveryTimeInMinutes = deliveryTimes.get(0);
            int hours = deliveryTimeInMinutes / 60;
            int minutes = deliveryTimeInMinutes % 60;

            return hours + ":" + minutes;
        }

       return "";
    }

    public void deletePartnerById(String partnerId) {
        orderDeliveryPartnerRepository.deletePartnerById(partnerId);
    }

    public String deleteOrderById(String orderId) {

        if(orderRepository.orderDb.containsKey(orderId)) {
            String partnerId = orderDeliveryPartnerRepository.orderDeliveryPairDb.get(orderId);

            List<String> orders = orderDeliveryPartnerRepository.getAllDeliveryPartnerOrders(partnerId);
            orders.remove(orderId);
            orderDeliveryPartnerRepository.deliveryPartnerOrdersDb.put(partnerId, orders);

            DeliveryPartner deliveryPartner = deliveryPartnerRepository.deliveryPartnerDb.get(partnerId);
            deliveryPartner.setNumberOfOrders(orders.size());
            deliveryPartnerRepository.deliveryPartnerDb.put(partnerId, deliveryPartner);

            orderRepository.orderDb.remove(orderId);
            List<String> assignedDeliveryPartners = orderDeliveryPartnerRepository.getAssignedDeliverPartnerIds();

            return "removed";
        }

        return null;
    }
}
