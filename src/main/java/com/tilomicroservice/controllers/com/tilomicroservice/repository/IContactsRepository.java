package com.tilomicroservice.controllers.com.tilomicroservice.repository;

import com.tilomicroservice.controllers.com.tilomicroservice.model.Contact;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
//import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface IContactsRepository extends CrudRepository<Contact, Long> {
    Contact findById(int id);

    @Query("SELECT t0 FROM Contact t0 WHERE UPPER(t0.phoneNumber) like :startsWith%")
    List<Contact> findByPhoneNumberStartsWithString(@Param("startsWith") String startsWith);
}
