package com.tilomicroservice.controllers.com.tilomicroservice.repository;


import com.tilomicroservice.controllers.com.tilomicroservice.model.Contact;

import java.util.HashMap;
import java.util.Random;

/**
 * Created by 212562776 on 7/14/17.
 */
public abstract class ContactsRepository implements IContactsRepository {
    public HashMap<Integer, Contact> map2Contact = new HashMap<>();
    public ContactsRepository() {
        Contact c = new Contact();
        c.setId(1);
        c.setAddress("Fremont Ca 94536");
        c.setFirstName("Tilo");
        c.setLastName("G");
        map2Contact.put(1, c);
    }

    @Override
    public Contact findById( int id) {

        return map2Contact.get(id);
    }

    @Override
    public Contact saveAndFlush(Contact c) {

        if (map2Contact.containsKey(c.getId()) && c.getId()> 0) {
            map2Contact.put(c.getId(), c);
        }
        else {
            int range = 100;
            Random r = new Random();
            int number = (int)(r.nextInt()*range);
            c.setId(number);
            map2Contact.put(number,c);
        }

        return c;
    }
}
