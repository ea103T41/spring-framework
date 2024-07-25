package com.euphy.learn.model;

import com.euphy.learn.model.common.IOperations;

import java.util.List;

public interface CustomerDao extends IOperations<Customer> {

    Customer findByName(String name);
    List<Customer> findByAge(Integer age);

}
