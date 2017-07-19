package com.tilomicroservice;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tilomicroservice.model.Contact;
import com.tilomicroservice.repository.IContactsRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = com.tilomicroservice.Application.class)
@AutoConfigureMockMvc
public class ContactsControllerTest {

    @Autowired
    private MockMvc mvc;

    @Mock
    IContactsRepository mockRepository;
    ObjectMapper objectMapper;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        objectMapper = new ObjectMapper()
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
    }

    @Test
    public void test_getPhoneContactsByAreaCode_returnsMeaningfulResult() throws Exception {
        Contact c = new Contact();
        c.setPhoneNumber("5405532821");
        List<Contact> contactList = new ArrayList<>();
        contactList.add(c);

        when(mockRepository.findByPhoneNumberStartsWithString("540")).thenReturn(contactList);

        mvc.perform(MockMvcRequestBuilders.get("/v1/contacts/search/phoneNumber?areaCode=540").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void test_getContactsByAreaCode_returnsBadRequest_ifAreaCodeIsNot3Numbered() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/v1/contacts/search/phoneNumber?areaCode=54").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void test_getContactsByState_returnsBadRequest_ifStateIsInvalid() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/v1/contacts/search/address?state=ca").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        mvc.perform(MockMvcRequestBuilders.get("/v1/contacts/search/address?state=XY").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        mvc.perform(MockMvcRequestBuilders.get("/v1/contacts/search/address?state=wv").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void test_getContactsByState_returnsOk_ifStateIsValid() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/v1/contacts/search/address?state=CA").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mvc.perform(MockMvcRequestBuilders.get("/v1/contacts/search/address?state=WV").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mvc.perform(MockMvcRequestBuilders.get("/v1/contacts/search/address?state=WY").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mvc.perform(MockMvcRequestBuilders.get("/v1/contacts/search/address?state=AK").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mvc.perform(MockMvcRequestBuilders.get("/v1/contacts/search/address?state=KS").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void test_getContactsByDateRange_returns400_ifStartAndEndEpochsInvalid() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/v1/contacts/search/lastContacted?start=a&end=b").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
