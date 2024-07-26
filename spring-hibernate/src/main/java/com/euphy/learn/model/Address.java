package com.euphy.learn.model;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import org.hibernate.envers.Audited;

@MappedSuperclass
public class Address {

    public Address(String road) {
        this.road = road;
    }

    public Address() {
    }

    @Column(name = "address")
    @Audited
    private String road;

    public String getRoad() {
        return road;
    }

    public void setRoad(String road) {
        this.road = road;
    }
}
