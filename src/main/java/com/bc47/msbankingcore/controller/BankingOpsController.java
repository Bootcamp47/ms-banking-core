package com.bc47.msbankingcore.controller;

import com.bc47.msbankingcore.client.Purchase;
import com.bc47.msbankingcore.service.BankingOpsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/banking/ops")
public class BankingOpsController {

    @Autowired
    private BankingOpsService bankingOpsService;

    // -------------------Grant a product to customer

    @PostMapping("/grant-product-to-customer")
    public Flux<Purchase> grantProductToCustomer(@RequestBody Purchase purchase) {
        return bankingOpsService.grantProductToCustomer(purchase);
    }

    // -------------------Retrieve customer products (purchases)

    @GetMapping("/retrieve-customer-products/{id}")
    public Flux<Purchase> retrieveCustomerPurchases(@PathVariable("id") String id) {
        return bankingOpsService.retrieveCustomerPurchases(id);
    }
}
