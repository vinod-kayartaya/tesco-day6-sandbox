package com.tesco.service;

import com.tesco.dto.CustomerRequestDto;
import com.tesco.dto.CustomerResponseDto;
import com.tesco.entity.Customer;
import com.tesco.exception.CustomerNotFoundException;
import com.tesco.exception.DuplicateCustomerException;
import com.tesco.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerResponseDto createCustomer(CustomerRequestDto requestDto) {
        log.debug("Creating new customer with email: {}", requestDto.getEmail());

        // Check if customer exists
        if (customerRepository.findByEmail(requestDto.getEmail()).isPresent()) {
            throw new DuplicateCustomerException("Customer already exists with email: " + requestDto.getEmail());
        }

        // Convert DTO to entity
        Customer customer = requestDtoToEntity(requestDto);

        // Save and convert back to response DTO
        Customer savedCustomer = customerRepository.save(customer);
        return entityToResponseDto(savedCustomer);
    }


    public CustomerResponseDto getCustomerById(String id) {
        var c = customerRepository.findById(id)
                .orElseThrow(() -> getCustomerNotFoundException(id));
        return entityToResponseDto(c);
    }


    public List<CustomerResponseDto> getAllCustomers() {
        log.debug("Fetching all customers");
        List<Customer> customers = customerRepository.findAll();
        return entityListToResponseDtoList(customers);
    }


    public CustomerResponseDto updateCustomer(String id, CustomerRequestDto requestDto) {
        log.debug("Updating customer with id: {}", id);

        Customer existingCustomer = customerRepository.findById(id)
                .orElseThrow(() -> getCustomerNotFoundException(id));

        // Update entity from DTO
        updateEntityFromDto(requestDto, existingCustomer);

        // Save and convert to response DTO
        Customer updatedCustomer = customerRepository.save(existingCustomer);
        return entityToResponseDto(updatedCustomer);
    }


    public void deleteCustomer(String id) {
        if (!customerRepository.existsById(id)) {
            throw getCustomerNotFoundException(id);
        }
        customerRepository.deleteById(id);
    }

    public List<CustomerResponseDto> getCustomersByCity(String city) {
        log.debug("Fetching customers from city: {}", city);
        List<Customer> customers = customerRepository.findByCity(city);
        return entityListToResponseDtoList(customers);
    }

    private CustomerResponseDto entityToResponseDto(Customer customer) {
        return CustomerResponseDto.builder()
                .id(customer.getId())
                .firstname(customer.getFirstname())  // note the case difference
                .lastname(customer.getLastname())    // note the case difference
                .email(customer.getEmail())
                .gender(customer.getGender())
                .city(customer.getCity())
                .phone(customer.getPhone())
                .build();
    }

    private Customer requestDtoToEntity(CustomerRequestDto requestDto) {
        return Customer.builder()
                .firstname(requestDto.getFirstname())
                .lastname(requestDto.getLastname())
                .email(requestDto.getEmail())
                .gender(requestDto.getGender())
                .city(requestDto.getCity())
                .phone(requestDto.getPhone())
                .build();
    }

    private List<CustomerResponseDto> entityListToResponseDtoList(List<Customer> customers) {
        return customers.stream()
                .map(this::entityToResponseDto)
                .toList();
    }

    private void updateEntityFromDto(CustomerRequestDto requestDto, Customer existingCustomer) {
        existingCustomer.setFirstname(requestDto.getFirstname());
        existingCustomer.setLastname(requestDto.getLastname());
        existingCustomer.setEmail(requestDto.getEmail());
        existingCustomer.setGender(requestDto.getGender());
        existingCustomer.setCity(requestDto.getCity());
        existingCustomer.setPhone(requestDto.getPhone());
    }

    private static CustomerNotFoundException getCustomerNotFoundException(String id) {
        return new CustomerNotFoundException("Customer not found with id: " + id);
    }
}