package com.learn.euphy.model;

import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class CustomerDaoWithEntityManager {

    private final EntityManager entityManager;

    @Autowired
    public CustomerDaoWithEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public Customer findById(Long id) {
        return entityManager.find(Customer.class, id);
    }

}
