package com.euphy.learn.model;

import com.euphy.learn.model.common.AbstractHibernateDao;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CustomerDaoImpl extends AbstractHibernateDao<Customer>
        implements CustomerDao {

    public CustomerDaoImpl() {
        super();
        setClazz(Customer.class);
    }

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public Customer findByName(String name) {
        return sessionFactory.openSession()
                .createQuery("from Customer where name = :name", Customer.class)
                .setParameter("name", name)
                .getResultList()
                .stream()
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Customer> findByAge(Integer age) {
        return sessionFactory.openSession()
                .createQuery("from Customer where age = :age", Customer.class)
                .setParameter("age", age)
                .getResultList();
    }

}
