package com.tilomicroservice.controllers;

/**
 * Created by 212562776 on 7/13/17.
 */
public interface IContactsService {

   Contact getContactById(int id);

   Contact saveContact(Contact contact) throws Exception;

   void deleteContact(Contact contact) throws Exception;

}
