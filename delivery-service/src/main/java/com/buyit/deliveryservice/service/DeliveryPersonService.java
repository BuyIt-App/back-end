package com.buyit.deliveryservice.service;

import com.buyit.deliveryservice.dto.DeliverPersonRes;
import com.buyit.deliveryservice.model.DeliveryPerson;
import com.buyit.deliveryservice.repository.DeliveryPersonRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DeliveryPersonService {
    private final DeliveryPersonRepo deliveryPersonRepo;

    public List<DeliverPersonRes> getAllDeliverPersons() {
        List<DeliveryPerson> deliveryPeoples = deliveryPersonRepo.findAll();
        return deliveryPeoples.stream()
                .map(this::mapToDeliverPersonRes)
                .collect(Collectors.toList());
    }

    public DeliverPersonRes findById(long id) {
        DeliveryPerson c = deliveryPersonRepo.findById(id).orElse(null);
        return c != null ? mapToDeliverPersonRes(c) : null;
    }

    private DeliverPersonRes mapToDeliverPersonRes(DeliveryPerson c) {
        DeliverPersonRes dp = new DeliverPersonRes();
        dp.setDeliveryPersonId(c.getId());
        dp.setEmailId(c.getEmailId());
        dp.setFirstName(c.getFirstName());
        dp.setLastName(c.getLastName());
        dp.setVehicleNumber(c.getVehicleNumber());
        dp.setPhoneNumber(c.getPhoneNumber());
        return dp;
    }
}
