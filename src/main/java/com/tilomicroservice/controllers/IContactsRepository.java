package com.tilomicroservice.controllers;

import org.springframework.stereotype.Repository;
//import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface IContactsRepository { //extends JpaRepository<Contact, Long> {
    Contact findById(int id);
    Contact saveAndFlush(Contact c);
}
