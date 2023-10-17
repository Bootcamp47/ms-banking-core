package com.bc47.msbankingcore.service;

import com.bc47.msbankingcore.client.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BankingOpsService {

    Flux<Purchase> grantProductToCustomer(Purchase purchase);
    Flux<Purchase> retrieveCustomerPurchases(String customerId);
    Mono<Transaction> deposit(String customerId, String purchaseId, double amount);
    Mono<Transaction> withdraw(String customerId, String purchaseId, double amount);
}
