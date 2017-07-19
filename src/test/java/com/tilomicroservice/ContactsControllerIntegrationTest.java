package com.tilomicroservice;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tilomicroservice.controllers.ContactsController;
import com.tilomicroservice.model.Contact;
import com.tilomicroservice.repository.IContactsRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.net.URL;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = com.tilomicroservice.Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
public class ContactsControllerIntegrationTest {

    @LocalServerPort
    private int port;

    private URL base;

    @Inject
    @InjectMocks
    ContactsController contactsController;

    @Inject
    IContactsRepository contactsRepository;

    @Autowired
    private TestRestTemplate template;

    @Autowired
    private MockMvc mvc;

    private ObjectMapper objectMapper;

    @Before
    public void setUp() throws Exception {
        this.base = new URL("http://localhost:" + port + "/v1/contacts");

        objectMapper = new ObjectMapper()
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
    }

    @Test
    public void testGetContactById() throws Exception {
        Contact contact = new Contact();
        contact.setFirstName("Harry");
        contact.setLastName("Potter");
        contact.setAddress("NY,NY,07310");
        contact.setPhoneNumber("2018878888");

        contactsRepository.save(contact);

        String json = mvc.perform(get(this.base + "/" + contact.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Contact contactResult = objectMapper.readValue(json, Contact.class);
        assertNotNull(contactResult.getId());
        assertEquals(contact.getFirstName(), contactResult.getFirstName());
    }

    @Test
    public void testGetContactsByAddress() throws Exception {
        Contact contact = new Contact();
        contact.setFirstName("Harry");
        contact.setLastName("Potter");
        contact.setAddress("NY,NY,07310");
        contact.setPhoneNumber("2018878888");
        contactsRepository.save(contact);

        Contact contact1 = new Contact();
        contact1.setFirstName("April");
        contact1.setLastName("Ludgate");
        contact1.setAddress("Pawnee,IN,44044");
        contact1.setPhoneNumber("4404444444");
        contactsRepository.save(contact1);

        String json = mvc.perform(get(this.base + "/search/address?state=IN")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();


        List<Contact> contactList = objectMapper.readValue(json, objectMapper.getTypeFactory().constructCollectionType(List.class, Contact.class));

        assertEquals(contactList.size(), 1);
        assertEquals(contactList.get(0).getFirstName(), contact1.getFirstName());
    }

    @Test
    public void testGetContactsByDateRange() throws Exception {
        Contact contact = new Contact();
        contact.setFirstName("Hermione");
        contact.setLastName("Granger");
        contact.setAddress("NY,NY,07310");
        contact.setPhoneNumber("2018888888");
        contact.setLastContacted(new Date(1402808366000L)); //June 16, 2014
        contactsRepository.save(contact);

        Contact contact1 = new Contact();
        contact1.setFirstName("Andy");
        contact1.setLastName("Dwyer");
        contact1.setAddress("Eagleton,IN,44044");
        contact1.setPhoneNumber("4414444444");
        contact1.setLastContacted(new Date(1465966766000L)); //June 16, 2016

        contactsRepository.save(contact1);

        Long startEpoch = 1451624366000L; //Jan 1, 2016
        Long endEpoch = 1483160366000L; //Dec 31, 2016


        String json = mvc.perform(get(this.base + "/search/lastContacted?start=" + startEpoch + "&end=" + endEpoch)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();


        List<Contact> contactList = objectMapper.readValue(json, objectMapper.getTypeFactory().constructCollectionType(List.class, Contact.class));

        assertEquals(contactList.size(), 1);
        assertEquals(contactList.get(0).getFirstName(), contact1.getFirstName());
    }

    @Test
    public void testUpdateContact() throws Exception {
        Contact originalContact = new Contact();
        originalContact.setFirstName("Leslie");
        originalContact.setLastName("Knope");
        originalContact.setPhoneNumber("4404444444");
        originalContact.setAddress("Pawnee,IN,44044");
        originalContact.setLastContacted(new Date(1402808366000L));

        // create in database
        String postedOriginalContactString = mvc.perform(post(this.base+"/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(objectMapper.valueToTree(originalContact))))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Contact postedOriginalContact = objectMapper.readValue(postedOriginalContactString, Contact.class);

        assertEquals(postedOriginalContact.getFirstName(), originalContact.getFirstName());
        assertEquals(postedOriginalContact.getLastContacted(), originalContact.getLastContacted());

        postedOriginalContact.setLastName("Knope-Wyatt");
        postedOriginalContact.setLastContacted(new Date(1465966766000L));

        // save update form
        mvc.perform(put(this.base + "/" + postedOriginalContact.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(objectMapper.valueToTree(postedOriginalContact))))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        // get the updated form
        String updatedFormFromGetString = mvc.perform(get(this.base + "/" + postedOriginalContact.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();

        Contact updatedFormFromGet = objectMapper.readValue(updatedFormFromGetString, Contact.class);

        assertEquals(updatedFormFromGet.getId(), postedOriginalContact.getId());
        assertEquals(updatedFormFromGet.getLastName(), "Knope-Wyatt");
        assertEquals(updatedFormFromGet.getLastContacted(), postedOriginalContact.getLastContacted());

    }

    @Test
    public void testUpdateContact_ReturnsBadRequest_IfStateIsInvalid() throws Exception {
        Contact originalContact = new Contact();
        originalContact.setFirstName("Leslie");
        originalContact.setLastName("Knope");
        originalContact.setPhoneNumber("4404444444");
        originalContact.setAddress("Pawnee,IN,44044");
        originalContact.setLastContacted(new Date(1402808366000L));

        // create in database
        String postedOriginalContactString = mvc.perform(post(this.base+"/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(objectMapper.valueToTree(originalContact))))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Contact postedOriginalContact = objectMapper.readValue(postedOriginalContactString, Contact.class);

        assertEquals(postedOriginalContact.getFirstName(), originalContact.getFirstName());
        assertEquals(postedOriginalContact.getLastContacted(), originalContact.getLastContacted());

        postedOriginalContact.setLastName("Knope-Wyatt");
        postedOriginalContact.setAddress("Pawnee,IK,44144");

        // save update form
        mvc.perform(put(this.base + "/" + postedOriginalContact.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(objectMapper.valueToTree(postedOriginalContact))))
                .andExpect(status().isBadRequest());
    }


}
