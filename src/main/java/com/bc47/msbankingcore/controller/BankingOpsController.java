package com.bc47.msbankingcore.controller;

import com.bc47.msbankingcore.client.Customer;
import com.bc47.msbankingcore.service.BankingOpsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/banking/ops")
public class BankingOpsController {

    @Autowired
    private BankingOpsService bankingOpsService;

    //    CUSTOMERS ********
    // -------------------Retrieve all customers

    @GetMapping("/customers")
    public ResponseEntity<List<Customer>> retrieveAllCustomers() {
        return bankingOpsService.retrieveAllCustomers();
    }
}
