package com.tilomicroservice.controllers.com.tilomicroservice.model;
//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by 212562776 on 7/13/17.
 */

@Entity
@Table(name="contacts")
public class Contact implements Serializable {
    private static final long serialVersionUID = -3009157732242241606L;

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;

    @Column(name = "fir stname")
    private String firstName;

    @Column(name= "lastname")
    private String lastName;

    @Column(name = "phonenumber")
    private String phoneNumber;

    @Column(name = "address")
    private String address;

    //private Date lastContacted;

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

    @Override
    public String toString() {
        return this.getId()+this.getFirstName()+this.getLastName()+this.getPhoneNumber()+this.getAddress();
    }
}
