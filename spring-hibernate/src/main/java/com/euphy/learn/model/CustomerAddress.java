package com.euphy.learn.model;

import jakarta.persistence.*;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;

@Entity
@Table(name = "t_address")
@Audited
@AuditTable("audit_t_address")
public class CustomerAddress extends Address {

    public CustomerAddress(String address) {
        super(address);
    }

    public CustomerAddress() {
    }

    @Id
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    // Use @JoinColumn with insertable=false and updatable=false for the customer_id field.
    // This will prevent Hibernate Envers from managing the column directly and thus avoid the conflict.
    @JoinColumn(name = "customer_id", nullable = false, insertable = false, updatable = false)
    @MapsId
    private Customer customer;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
