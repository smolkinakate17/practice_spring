package com.example.practice.services;


import com.example.practice.models.Customer;
import com.example.practice.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;

    public List<Customer> getAllCustomers(){
        return customerRepository.findAll();
    }
    public Customer getCustomerById(Long id){
        return customerRepository.findById(id).orElse(null);
    }
    public void saveCustomer(Customer customer){
        customerRepository.save(customer);
    }

}
