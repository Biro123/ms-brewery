package com.oldman.msbrewery.web.mappers;

import com.oldman.msbrewery.domain.Customer;
import com.oldman.msbrewery.web.model.CustomerDto;
import org.mapstruct.Mapper;

@Mapper
public interface CustomerMapper {

    CustomerDto customerToCustomerDto(Customer customer);
    Customer CustomerDtoToCustomer(CustomerDto dto);

}
