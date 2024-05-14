package com.learn.euphy.model;

import com.learn.euphy.config.Application;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest(classes = Application.class)
public class CustomerDaoTest {

    @Autowired
    CustomerDao customerDao;

    private List<Customer> users;

    @BeforeEach
    public void setUp() {
        customerDao.save(new Customer("AAA", 18, "SYSTEM"));
        users = new ArrayList<>();
        users.add(new Customer("BBB", 19, "SYSTEM"));
        users.add(new Customer("CCC", 20, "SYSTEM"));
    }

    @AfterEach
    public void tearDown() {
        customerDao.deleteAll();
        Assertions.assertEquals(0, customerDao.count());
    }

    @Test
    void findByName(){
        Customer customer = customerDao.findByName("AAA");
        Assertions.assertNotNull(customer);
        Assertions.assertEquals(18, customer.getAge());
    }

    @Test
    void findByAge(){
        List<Customer> customers = customerDao.findByAge(18);
        Assertions.assertEquals(1, customers.size());
        Assertions.assertEquals("AAA", customers.get(0).getName());
    }

    @Test
    void saveAll(){
        List<Customer> list = customerDao.saveAll(users);
        Assertions.assertEquals(2, list.size());
    }

    @Test
    void findAll(){
        customerDao.saveAll(users);
        Assertions.assertEquals(3, customerDao.findAll().size());
    }

    @Test
    void findAllWithSort(){
        customerDao.saveAll(users);
        List<Customer> customers = customerDao.findAll(Sort.by(Sort.Direction.DESC,"name"));
        Assertions.assertEquals("CCC", customers.get(0).getName());
    }

    @Test
    void findAllWithPage(){
        customerDao.saveAll(users);
        Page<Customer> page = customerDao.findAll(Pageable.ofSize(2).withPage(0));
        Assertions.assertEquals(2, page.getContent().size());
        Assertions.assertEquals(3, page.getTotalElements());
        Assertions.assertEquals(2, page.getTotalPages());
    }

}
