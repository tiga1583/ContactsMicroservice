package com.tilomicroservice.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/v1/contacts")
public class ContactsController {

    IContactsService contactsService = new ContactsService();

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    ResponseEntity<?> getContactById(@PathVariable("id") Integer id, HttpServletRequest request) {

        try {
            Contact c = contactsService.getContactById(id);
            if(c == null) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(c, HttpStatus.OK);
        } catch (Exception e) {
            //Add logging
            return new ResponseEntity<>(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.toString(), e.getMessage(), request.getRequestURI()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
