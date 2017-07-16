package com.tilomicroservice.controllers.com.tilomicroservice.service;

import com.tilomicroservice.controllers.com.tilomicroservice.repository.IContactsRepository;
import com.tilomicroservice.controllers.com.tilomicroservice.model.Contact;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by 212562776 on 7/13/17.
 */
public class ContactsService implements IContactsService {

    @Autowired
    IContactsRepository contactsRepository; //= new ContactsRepository();

    @Override
    public Contact getContactById(int id) {
        System.out.println("Are we getting this repository?--"+contactsRepository);
        return contactsRepository.findById(id);
    }

    @Override
    public Contact saveContact(Contact contact) throws Exception {
        contactsRepository.save(contact);
        return contactsRepository.findById(contact.getId());
    }

    @Override
    public void deleteContact(Contact contact) {
       // = contactsRepository.findById(contact.getId());
       //contactsRepository.delete(contact);
    }

//    public void getContactsByState(String state) {
//
//    }


}
