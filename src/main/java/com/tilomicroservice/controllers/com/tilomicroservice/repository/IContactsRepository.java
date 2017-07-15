package com.tilomicroservice.controllers.com.tilomicroservice.repository;

import com.tilomicroservice.controllers.com.tilomicroservice.model.Contact;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
//import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface IContactsRepository extends CrudRepository<Contact, Long> {
    Contact findById(int id);
    Contact saveAndFlush(Contact c);
}
