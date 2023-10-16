package com.bc47.msbankingcore.service;

import com.bc47.msbankingcore.client.*;
import org.springframework.http.ResponseEntity;
import java.util.List;

public interface BankingOpsService {

    ResponseEntity<List<Customer>> retrieveAllCustomers();
    ResponseEntity<Customer> retrieveCustomer(Long id);
    ResponseEntity<Customer> saveCustomer(Customer customer);
    ResponseEntity<Customer> updateCustomer(Customer customer);
    ResponseEntity<Customer> deleteCustomer(Long id);

    ResponseEntity<List<Product>> retrieveAllProducts();
    ResponseEntity<Product> retrieveProduct(Long id);
    ResponseEntity<Product> saveProduct(Product product);
    ResponseEntity<Product> updateProduct(Product product);
    ResponseEntity<Product> deleteProduct(Long id);

    ResponseEntity<List<Purchase>> retrieveAllPurchases();
    ResponseEntity<Purchase> retrievePurchase(Long id);
    ResponseEntity<Purchase> savePurchase(Purchase purchase);
    ResponseEntity<Purchase> updatePurchase(Purchase purchase);
    ResponseEntity<Purchase> deletePurchase(Long id);

    ResponseEntity<List<Transaction>> retrieveAllTransactions();
    ResponseEntity<Transaction> retrieveTransaction(Long id);
    ResponseEntity<Transaction> saveTransaction(Transaction transaction);
    ResponseEntity<Transaction> updateTransaction(Transaction transaction);
    ResponseEntity<Transaction> deleteTransaction(Long id);

    ResponseEntity<Purchase> grantProductToCustomer(String customerDocNumber, String productCategory);
    ResponseEntity<List<Purchase>> retrieveCustomerPurchases(Long customerId);
    ResponseEntity<Transaction> deposit(Long customerId, Long purchaseId, double amount);
    ResponseEntity<Transaction> withdraw(Long customerId, Long purchaseId, double amount);
}
