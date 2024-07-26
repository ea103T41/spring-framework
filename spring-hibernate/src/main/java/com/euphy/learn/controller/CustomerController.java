package com.euphy.learn.controller;

import com.euphy.learn.dto.CustomerDto;
import com.euphy.learn.model.Customer;
import com.euphy.learn.model.CustomerAddress;
import com.euphy.learn.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/customer")
public class CustomerController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public ResponseEntity<?> findCustomer(
            @RequestParam(name = "name", required = false) String name) {
        if (name == null) {
            return ResponseEntity.badRequest().body("customer name is required");
        }
        Customer customer = customerService.findByName(name);
        if (customer == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(toDto(customer));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDto> findCustomerById(@PathVariable("id") Long id) {
        Customer customer = customerService.findOneById(id);
        if (customer == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(toDto(customer));
    }

    @GetMapping("/age/{age}")
    public List<CustomerDto> findCustomerByAge(@PathVariable("age") Integer age) {
        List<Customer> customers = customerService.findByAge(age);
        return customers.stream()
                .map(this::toDto)
                .toList();
    }

    @PostMapping
    public ResponseEntity<?> saveCustomer(@RequestBody CustomerDto customerDto) {
        Customer existCustomer = customerService.findByName(customerDto.name());
        if (existCustomer != null) {
            return ResponseEntity.badRequest().body("customer already exists");
        }

        Customer customer;
        try {
            customer = fromDto(customerDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        customerService.saveCustomer(customer);
        return ResponseEntity.ok(toDto(customer));
    }

    @DeleteMapping
    public ResponseEntity<String> delCustomer(@RequestParam(name = "name") String name) {
        Customer customer = customerService.findByName(name);
        if (customer == null) {
            return ResponseEntity.notFound().build();
        }
        customerService.delCustomerById(customer.getId());
        return ResponseEntity.ok("success");
    }

    private Customer fromDto(CustomerDto customerDto) {
        if (customerDto.name() == null || customerDto.age() == null || customerDto.createBy() == null) {
            throw new IllegalArgumentException("name, age and createBy are required");
        }
        Customer customer = new Customer(customerDto.name(), customerDto.age(), customerDto.createBy());
        CustomerAddress address = new CustomerAddress(customerDto.address());
        customer.setAddress(address);
        address.setCustomer(customer);
        return customer;
    }

    private CustomerDto toDto(Customer customer) {
        return new CustomerDto(customer.getName(), customer.getAge(), customer.getCreateBy(),
                customer.getAddress().getRoad());
    }

}
