package com.tilomicroservice.controllers;

import com.tilomicroservice.model.Contact;
import com.tilomicroservice.repository.IContactsRepository;
import com.tilomicroservice.service.UtilitiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/v1/contacts")
public class ContactsController {

    @Autowired
    IContactsRepository contactsRepository;

    //Need to dependency inject this
    UtilitiesService service = new UtilitiesService();

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
            //contactsRepository.save(contact);
            return new ResponseEntity<>(contactsRepository.save(contact), HttpStatus.CREATED);
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

        if (!isContactValid(contact)) {
            return new ResponseEntity<>(new ErrorResponse(HttpStatus.BAD_REQUEST.toString(), "Invalid contact - phone or address not in the right format", request.getRequestURI()), HttpStatus.BAD_REQUEST);
        }

        try {
            return new ResponseEntity<>(contactsRepository.save(contact), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ErrorResponse(HttpStatus.BAD_REQUEST.toString(), e.getMessage(), request.getRequestURI()), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value="/search/phoneNumber", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    ResponseEntity<?> getContactsWithAreaCode(@RequestParam(value = "areaCode") String areaCode, HttpServletRequest request) {
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
            e.printStackTrace();
            return new ResponseEntity<>(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.toString(), e.getMessage(), request.getRequestURI()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value="/search/address", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    ResponseEntity<?> getContactsWithState(@RequestParam(value = "state") String state, HttpServletRequest request) {
        try {

            if (state == null || "".equals(state) || !state.matches("^[A-Z]{2}$") || !service.isValidStateAbbrev(state)) {
                return new ResponseEntity<>(new ErrorResponse(HttpStatus.BAD_REQUEST.toString(), "Invalid state", request.getRequestURI()), HttpStatus.BAD_REQUEST);
            }

            List<Contact> contactList = contactsRepository.findAddressContainingState(","+state+",");

            if(contactList == null) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(contactList, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.toString(), e.getMessage(), request.getRequestURI()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value="/search/lastContacted", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    ResponseEntity<?> getContactsWithLastContactedBetweenStartAndEndDates(@RequestParam(value = "start") String startDate, @RequestParam(value="end")String endDate, HttpServletRequest request) {
        try {

            if (startDate == null || endDate == null) {
                return new ResponseEntity<>(new ErrorResponse(HttpStatus.BAD_REQUEST.toString(), "Invalid date", request.getRequestURI()), HttpStatus.BAD_REQUEST);
            }

            Date startEpoch = new Date(Long.parseLong(startDate));
            Date endEpoch = new Date(Long.parseLong(endDate));

            List<Contact> contactList = contactsRepository.findByLastContactedBetween(startEpoch, endEpoch);

            if (contactList == null) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(contactList, HttpStatus.OK);
        } catch (NumberFormatException nfe) {

            nfe.printStackTrace();
            return new ResponseEntity<>(new ErrorResponse(HttpStatus.BAD_REQUEST.toString(), "start and end should be milliseconds from epoch", request.getRequestURI()), HttpStatus.BAD_REQUEST);
        }
        catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.toString(), e.getMessage(), request.getRequestURI()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private boolean isContactValid(Contact c) {

        boolean isValid = true;
        if (c.getPhoneNumber() != null) {
            isValid = c.getPhoneNumber().matches("\\d{10}");
        }

        if (isValid && c.getAddress() != null) {
            Pattern p = Pattern.compile("^.*,([A-Z]{2}),\\d{5}$");
            Matcher m = p.matcher(c.getAddress());
            if(m.matches()) {
                String state = m.group(1);
                isValid = service.isValidStateAbbrev(state);
            }
            else {
                isValid = false;
            }
        }

        return isValid;
    }

}
