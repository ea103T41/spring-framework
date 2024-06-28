package com.euphy.learn.service;

import com.euphy.learn.model.CustomerDaoWithEntityManager;
import com.euphy.learn.model.Customer;
import com.euphy.learn.model.CustomerDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerDao customerDao;
    private final CustomerDaoWithEntityManager customerDao2;

    @Autowired
    public CustomerServiceImpl(CustomerDao customerDao, CustomerDaoWithEntityManager customerDao2) {
        this.customerDao = customerDao;
        this.customerDao2 = customerDao2;
    }

    @Override
    public void saveCustomer(Customer customer) {
        if (customer.getCreateBy() == null) {
            customer.setCreateBy("system");
        }
        customerDao.save(customer);
    }

    @Override
    public void delCustomerById(Long id) {
        customerDao.deleteById(id);
    }

    @Override
    public void delAll() {
        customerDao.deleteAll();
    }

    @Override
    public Customer findOneById(Long id) {
//        return customerDao.findById(id).orElse(null);
        return customerDao2.findById(id);
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
