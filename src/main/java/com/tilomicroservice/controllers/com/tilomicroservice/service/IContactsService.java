package com.tilomicroservice.controllers.com.tilomicroservice.service;

import com.tilomicroservice.controllers.com.tilomicroservice.model.Contact;

/**
 * Created by 212562776 on 7/13/17.
 */
public interface IContactsService {

   Contact getContactById(int id);

   Contact saveContact(Contact contact) throws Exception;

   void deleteContact(Contact contact) throws Exception;

}
