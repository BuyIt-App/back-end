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
            if(c.getRole().equals("CUSTOMER")) {
                CustomerRes cr = new CustomerRes();
                cr.setCustomerId(c.getCustomerId());
                cr.setEmailId(c.getEmailId());
                cr.setFirstName(c.getFirstName());
                cr.setLastName(c.getLastName());
                cr.setPhoneNumber(c.getPhoneNumber());
                customerResList.add(cr);
            }
        }

        return customerResList;

    }

    public CustomerRes findById(long id) {
        Customer c = customerRepo.findById((int) id).get();
        CustomerRes cr = new CustomerRes();
        cr.setCustomerId(c.getCustomerId());
        cr.setEmailId(c.getEmailId());
        cr.setFirstName(c.getFirstName());
        cr.setLastName(c.getLastName());
        cr.setPhoneNumber(c.getPhoneNumber());

        return cr;
    }

    public CustomerRes updateCustomer(CustomerRes customerRes, long customerId) {
            Customer customer = customerRepo.findById((int) customerId).get();
            customerRes.setCustomerId(customerId);
            customer.setEmailId(customerRes.getEmailId());
            customer.setFirstName(customerRes.getFirstName());
            customer.setLastName(customerRes.getLastName());
            customer.setPhoneNumber(customerRes.getPhoneNumber());
            customerRepo.save(customer);

            return  customerRes;
        }

}
