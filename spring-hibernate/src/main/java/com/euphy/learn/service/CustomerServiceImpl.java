package com.euphy.learn.service;

import com.euphy.learn.model.Customer;
import com.euphy.learn.model.CustomerDao;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerDao customerDao;

    @Autowired
    public CustomerServiceImpl(CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    @Transactional
    @Override
    public void saveCustomer(Customer customer) {
        if (customer.getCreateBy() == null) {
            customer.setCreateBy("system");
        }
        customerDao.save(customer);
    }

    @Transactional
    @Override
    public void delCustomerById(Long id) {
        Customer existCustomer = customerDao.findById(id);
        if (existCustomer == null) {
            throw new RuntimeException("Customer not found");
        }
        customerDao.delete(existCustomer);
    }

    @Transactional
    @Override
    public void delAll() {
        customerDao.deleteAll();
    }

    @Override
    public Customer findOneById(Long id) {
        return customerDao.findById(id);
    }

    @Override
    public Customer findByName(String name) {
        return customerDao.findByName(name);
    }

    @Override
    public List<Customer> findByAge(Integer age) {
        return customerDao.findByAge(age);
    }

}
