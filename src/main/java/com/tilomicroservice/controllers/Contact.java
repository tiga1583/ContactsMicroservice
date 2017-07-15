package com.tilomicroservice.controllers;
//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
import java.util.Date;

/**
 * Created by 212562776 on 7/13/17.
 */

//@Entity
public class Contact {

    //@Id
    //@GeneratedValue(strategy= GenerationType.AUTO)
    private int id;

    private String firstName;

    private String lastName;

    private String phoneNumber;

//    @OneToMany(mappedBy = "contact", targetEntity = Address.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
//    @JsonManagedReference(value = "address")
//    private Address address;

    private String address;

    private Date lastContacted;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
