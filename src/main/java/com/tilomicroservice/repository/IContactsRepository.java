package com.tilomicroservice.repository;

import com.tilomicroservice.model.Contact;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface IContactsRepository extends CrudRepository<Contact, Long> {
    Contact findById(int id);

    @Query("SELECT t0 FROM Contact t0 WHERE UPPER(t0.phoneNumber) like :startsWith%")
    List<Contact> findByPhoneNumberStartsWithString(@Param("startsWith") String startsWith);

    @Query("SELECT t0 FROM Contact t0 WHERE t0.address like %:state%")
    List<Contact> findAddressContainingState(@Param("state") String state);

    List<Contact> findByLastContactedBetween(Date start, Date end);

}
