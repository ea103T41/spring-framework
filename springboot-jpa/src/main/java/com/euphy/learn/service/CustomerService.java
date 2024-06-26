package com.euphy.learn.service;

import com.euphy.learn.model.Customer;

import java.util.List;

public interface CustomerService {

    void saveCustomer(Customer customer);
    void delCustomerById(Long id);
    void delAll();
    Customer findOneById(Long id);
    Customer findByName(String name);
    List<Customer> findByAge(Integer age);

}
