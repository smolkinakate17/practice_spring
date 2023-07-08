package com.example.practice.services;


import com.example.practice.models.Customer;
import com.example.practice.repositories.CustomerRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.hibernate.exception.SQLGrammarException;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;

    public List<Customer> getAllCustomers(){
        return customerRepository.findAll();
    }

    public Customer getCustomerById(Long id){
        if(customerRepository.existsById(id)){
            return customerRepository.getReferenceById(id);
        }
        else return null;
    }
    public void saveCustomer(Customer customer){
        customerRepository.save(customer);
    }

    public void updateCustomerById(Customer newcustomer){
        Long id = newcustomer.getId();
        Customer oldcustomer=customerRepository.getReferenceById(id);
        oldcustomer.setTitle(newcustomer.getTitle());
        customerRepository.save(oldcustomer);
    }
    public void deleteCustomer(Customer customer){
        customerRepository.delete(customer);
    }


}
