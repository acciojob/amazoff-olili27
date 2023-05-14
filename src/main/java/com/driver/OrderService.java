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

    public void addOrder(Order order) {
        orderRepository.addOrder(order);
    }
    public void addPartner(String partnerId) {
        DeliveryPartnerRepository.addPartner(partnerId);
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
        List<DeliveryPartner> deliverPartners = DeliveryPartnerRepository.getAllDeliveryPartners();

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
//        List<String> orderIds =
                return orderDeliveryPartnerRepository.getAllDeliveryPartnerOrders(partnerId);

//        List<Order> orders = new ArrayList<>();
//
//        if (orderIds != null) {
//            for (String id: orderIds) {
//                orders.add(OrderRepository.orderDb.get(id));
//            }
//
//            return orders;
//        }
//
//        return null;
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
                DeliveryPartner deliveryPartner = DeliveryPartnerRepository.deliveryPartnerDb.get(partnersId);

                countAssignedOrders += deliveryPartner.getNumberOfOrders();
            }
        }

      return countAssignedOrders;
    }

    public int numberOfUnAssignedOrders() {
        List<Order> orders = orderRepository.getAllOrders();

        int assignedOrders = numberOfAssignedOrders();

        if (orders != null) {
            return orders.size() - assignedOrders;
        }

       return 0;
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
        DeliveryPartnerRepository.deletePartnerById(partnerId);
        orderDeliveryPartnerRepository.deletePartnerById(partnerId);
    }

    public String deleteOrderById(String orderId) {
        List<String> assignedDeliveryPartners = orderDeliveryPartnerRepository.getAssignedDeliverPartnerIds();

        if (assignedDeliveryPartners != null) {
            for (String id : assignedDeliveryPartners) {
                List<String> orderIds = orderDeliveryPartnerRepository.orderDeliveryPairDb.get(id);

                if (orderIds != null) {
                    orderIds.remove(orderId);
                    break;
                }
            }

            return "removed";
        }

        return null;
    }
}
