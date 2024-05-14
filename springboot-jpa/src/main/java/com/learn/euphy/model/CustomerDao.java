package com.learn.euphy.model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerDao extends JpaRepository<Customer, Long> {

    Customer findByName(String name);
    List<Customer> findByAge(Integer age);

}
