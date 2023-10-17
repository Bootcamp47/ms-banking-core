package com.bc47.msbankingcore.service;

import com.bc47.msbankingcore.client.*;
import reactor.core.publisher.Flux;

public interface BankingOpsService {

    Flux<Purchase> grantProductToCustomer(Purchase purchase);
    Flux<Purchase> retrieveCustomerPurchases(String customerId);
    Flux<Transaction> deposit(String customerId, String purchaseId, double amount);
    Flux<Transaction> withdraw(String customerId, String purchaseId, double amount);
    Flux<Transaction> retrieveCustomerPurchaseMovements(String customerId, String purchaseId);
}
