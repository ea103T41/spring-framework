package com.euphy.learn;

import com.euphy.learn.model.Customer;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static java.lang.System.out;

public class HibernateTest {

    private SessionFactory sessionFactory;

    @BeforeEach
    public void setUp() {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder().build();
        try {
            sessionFactory = new MetadataSources(registry)
                    .addAnnotatedClass(Customer.class)
                    .buildMetadata()
                    .buildSessionFactory();
        } catch (Exception e) {
            // The registry would be destroyed by the SessionFactory, but we
            // had trouble building the SessionFactory so destroy it manually.
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }

    @AfterEach
    public void tearDown() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

    @Test
    public void testBasicUsage() {
        // create a couple of customers...
        sessionFactory.inTransaction(session -> {
            session.persist(new Customer("A", 30, "Admin"));
            session.persist(new Customer("B", 40, "Admin"));
        });

        // now lets pull customers from the database and list them
        sessionFactory.inTransaction(session -> {
            session.createSelectionQuery("from Customer", Customer.class).getResultList()
                    .forEach(customer -> out.println("Customer (" + customer.getId() + ") : " + customer.getName()));
        });
    }
}
