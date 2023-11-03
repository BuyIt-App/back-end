package com.buyit.customerservice.service;

import com.buyit.customerservice.dto.responseDTO.CustomerRes;
import com.buyit.customerservice.model.Customer;
import com.buyit.customerservice.repository.CustomerRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepo customerRepo;

    public List<CustomerRes> getAllCustomers() {
        List<Customer> customers= customerRepo.findAll();
        List<CustomerRes> customerResList = new ArrayList<>();
        for(Customer c:customers){
            CustomerRes cr = new CustomerRes();
            cr.setCustomerId(c.getCustomerId());
            cr.setEmailId(c.getEmailId());
            cr.setFirstName(c.getFirstName());
            cr.setLastName(c.getLastName());
            cr.setPhoneNumber(c.getPhoneNumber());
            cr.setOrders(c.getOrders());
            customerResList.add(cr);
        }

        return customerResList;

    }
}
