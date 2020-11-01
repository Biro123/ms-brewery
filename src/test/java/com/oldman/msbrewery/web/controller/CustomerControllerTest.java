package com.oldman.msbrewery.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oldman.msbrewery.services.CustomerService;
import com.oldman.msbrewery.web.model.CustomerDto;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.StringUtils;

// IMPORTANT!! MockMVC requestBuilders must be replaced with RestDocs Builders !!
//    as per the below 2 lines
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(RestDocumentationExtension.class)
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "dev.oldmangames.brewery", uriPort = 80)
@WebMvcTest(CustomerController.class)
@ComponentScan(basePackages = "com.oldman.msbrewery.web.mappers")
class CustomerControllerTest {

    @MockBean
    CustomerService customerService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    CustomerDto validCustomer;

    private static final String CUSTOMER_NAME = "Joe";

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

        mockMvc.perform(get("/api/v1/customer/{customerId}", validCustomer.getId()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", Is.is(validCustomer.getId().toString())))
                .andExpect(jsonPath("$.name", Is.is(CUSTOMER_NAME)))
                .andDo(document("v1/customer-get",
                        pathParameters(
                                parameterWithName("customerId").description("UUID of Customer")
                        ),
                        responseFields(
                                fieldWithPath("id").description("Id of Customer"),
                                fieldWithPath("name").description("Customer Name")
                        )
                ));

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

        // Get constraint descriptions for restdocs
        ConstrainedFields fields = new ConstrainedFields((CustomerDto.class));

        mockMvc.perform(post("/api/v1/customer/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(customerJson))
                .andExpect(status().isCreated())
                .andDo(document("v1/customer-new",
                        requestFields(
                                fields.withPath("id").ignored(),
                                fields.withPath("name").description("Customer Name")
                        )
                ));

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

        // Get constraint descriptions for restdocs
        ConstrainedFields fields = new ConstrainedFields((CustomerDto.class));

        mockMvc.perform(put("/api/v1/customer/{customerId}", savedCustomer.getId().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(customerJson))
                .andExpect(status().isNoContent())
                .andDo(document("v1/customer-update",
                        requestFields(
                                fields.withPath("id").ignored(),
                                fields.withPath("name").description("Customer Name")
                        )
                ));
    }

    @Test
    void deleteBeer() {
    }

    // config for RestDocs to document constraints (using template - request-fields.snippet)
    private static class ConstrainedFields {

        private final ConstraintDescriptions constraintDescriptions;

        ConstrainedFields(Class<?> input) {
            this.constraintDescriptions = new ConstraintDescriptions(input);
        }

        private FieldDescriptor withPath(String path) {
            return fieldWithPath(path).attributes(key("constraints").value(StringUtils
                    .collectionToDelimitedString(this.constraintDescriptions
                            .descriptionsForProperty(path), ". ")));
        }
    }
}