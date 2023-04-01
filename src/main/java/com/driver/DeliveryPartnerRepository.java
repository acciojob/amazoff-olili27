package com.driver;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class DeliveryPartnerRepository {
    static HashMap<String, DeliveryPartner> deliveryPartnerDb = new HashMap<>();

    static public void addPartner(String partnerId) {
        DeliveryPartner deliveryPartner = new DeliveryPartner(partnerId);

        deliveryPartnerDb.put(partnerId, deliveryPartner);
    }

    static public List<DeliveryPartner> getAllDeliveryPartners() {
        if (deliveryPartnerDb.isEmpty()) return null;

        return deliveryPartnerDb.values().stream().collect(Collectors.toList());
    }

    static public void deletePartnerById(String partnerId) {
        if (deliveryPartnerDb.isEmpty() || !deliveryPartnerDb.containsKey(partnerId)) {
            return;
        }

        deliveryPartnerDb.remove(partnerId);
    }
}
