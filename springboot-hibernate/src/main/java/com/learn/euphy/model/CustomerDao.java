package com.learn.euphy.model;

import com.learn.euphy.model.common.IOperations;

import java.util.List;

public interface CustomerDao extends IOperations<Customer> {

    Customer findByName(String name);
    List<Customer> findByAge(Integer age);

}
