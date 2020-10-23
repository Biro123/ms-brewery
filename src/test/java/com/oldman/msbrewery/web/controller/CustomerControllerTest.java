package com.oldman.msbrewery.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oldman.msbrewery.services.CustomerService;
import com.oldman.msbrewery.web.model.CustomerDto;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    @MockBean
    CustomerService customerService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    CustomerDto validCustomer;

    private static String CUSTOMER_NAME = "Joe";

    @BeforeEach
    void setUp() {
        validCustomer = CustomerDto.builder()
                .id(UUID.randomUUID())
                .name(CUSTOMER_NAME)
                .build();
    }

    @Test
    void getCustomer() throws Exception {

        given(customerService.getCustomerById(any(UUID.class))).willReturn(validCustomer);

        mockMvc.perform(get("/api/v1/customer/" + validCustomer.getId()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", Is.is(validCustomer.getId().toString())))
                .andExpect(jsonPath("$.name", Is.is(CUSTOMER_NAME)));

    }

    @Test
    void handlePost() throws Exception {

        // mock result
        CustomerDto savedCustomer = CustomerDto.builder().id(UUID.randomUUID()).name("New Customer").build();
        given(customerService.saveNewCustomer(any())).willReturn(savedCustomer);

        // setup request
        CustomerDto customer = validCustomer;
        customer.setId(null);
        String customerJson = objectMapper.writeValueAsString(customer);

        mockMvc.perform(post("/api/v1/customer/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(customerJson))
                .andExpect(status().isCreated());

    }

    @Test
    void handleUpdate() throws Exception {

        // mock result
        CustomerDto savedCustomer = CustomerDto.builder().id(UUID.randomUUID()).name("New Customer").build();
        given(customerService.saveNewCustomer(any())).willReturn(savedCustomer);

        // setup request
        CustomerDto customer = validCustomer;
        customer.setId(null);
        String customerJson = objectMapper.writeValueAsString(customer);

        mockMvc.perform(put("/api/v1/customer/" + savedCustomer.getId().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(customerJson))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteBeer() {
    }
}