package com.tilomicroservice.controllers.com.tilomicroservice.controllers;

import com.tilomicroservice.controllers.com.tilomicroservice.model.Contact;
import com.tilomicroservice.controllers.com.tilomicroservice.repository.IContactsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/v1/contacts")
public class ContactsController {

    @Autowired
    IContactsRepository contactsRepository;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    ResponseEntity<?> getContactById(@PathVariable("id") Integer id, HttpServletRequest request) {

        try {
            Contact c = contactsRepository.findById(id);
            if(c == null) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(c, HttpStatus.OK);
        } catch (Exception e) {
            //Add logging
            return new ResponseEntity<>(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.toString(), e.getMessage(), request.getRequestURI()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<?> saveContact(@RequestBody Contact contact, HttpServletRequest request) {
        try {
            contactsRepository.save(contact);
            return new ResponseEntity<>("", HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.toString(), e.getMessage(), request.getRequestURI()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> saveContact(@PathVariable("id") Integer id, @RequestBody Contact contact, HttpServletRequest request) {
        if (contact.getId() == null || contact.getId() == 0) {
            contact.setId(id);
        }
        if (!contact.getId().equals(id)) {
            return new ResponseEntity<>(new ErrorResponse(HttpStatus.CONFLICT.toString(), "Invalid contact id", request.getRequestURI()), HttpStatus.CONFLICT);
        }
        try {
            return new ResponseEntity<>(contactsRepository.save(contact), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ErrorResponse(HttpStatus.BAD_REQUEST.toString(), e.getMessage(), request.getRequestURI()), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    ResponseEntity<?> getContactById(@RequestParam(value = "areaCode") String areaCode, HttpServletRequest request) {
        try {

            if (areaCode == null || "".equals(areaCode) || !areaCode.matches("^\\d{3}$")) {
                return new ResponseEntity<>(new ErrorResponse(HttpStatus.BAD_REQUEST.toString(), "Invalid areaCode", request.getRequestURI()), HttpStatus.BAD_REQUEST);
            }

            List<Contact> contactList = contactsRepository.findByPhoneNumberStartsWithString(areaCode);

            if(contactList == null) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(contactList, HttpStatus.OK);
        } catch (Exception e) {
            //Add logging
            return new ResponseEntity<>(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.toString(), e.getMessage(), request.getRequestURI()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
