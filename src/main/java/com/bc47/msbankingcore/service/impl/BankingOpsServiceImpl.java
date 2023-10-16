package com.bc47.msbankingcore.service.impl;

import com.bc47.msbankingcore.client.Customer;
import com.bc47.msbankingcore.client.Product;
import com.bc47.msbankingcore.client.Purchase;
import com.bc47.msbankingcore.client.Transaction;
import com.bc47.msbankingcore.service.BankingOpsService;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.util.List;

import static com.bc47.msbankingcore.util.Constants.*;

@Service
public class BankingOpsServiceImpl implements BankingOpsService {

    private WebClient webClient;

    @Override
    public ResponseEntity<List<Customer>> retrieveAllCustomers() {
        webClient = WebClient.create("http://localhost:8092");
        List<Customer> customers;
        Mono<List<Customer>> response = webClient.get()
                .uri(CUSTOMERS_PATH)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<>() {
                });
        customers = response.block();
        return new ResponseEntity<>(customers, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Customer> retrieveCustomer(Long id) {
        return null;
    }

    @Override
    public ResponseEntity<Customer> saveCustomer(Customer customer) {
        return null;
    }

    @Override
    public ResponseEntity<Customer> updateCustomer(Customer customer) {
        return null;
    }

    @Override
    public ResponseEntity<Customer> deleteCustomer(Long id) {
        return null;
    }

    @Override
    public ResponseEntity<List<Product>> retrieveAllProducts() {
        return null;
    }

    @Override
    public ResponseEntity<Product> retrieveProduct(Long id) {
        return null;
    }

    @Override
    public ResponseEntity<Product> saveProduct(Product product) {
        return null;
    }

    @Override
    public ResponseEntity<Product> updateProduct(Product product) {
        return null;
    }

    @Override
    public ResponseEntity<Product> deleteProduct(Long id) {
        return null;
    }

    @Override
    public ResponseEntity<List<Purchase>> retrieveAllPurchases() {
        return null;
    }

    @Override
    public ResponseEntity<Purchase> retrievePurchase(Long id) {
        return null;
    }

    @Override
    public ResponseEntity<Purchase> savePurchase(Purchase purchase) {
        return null;
    }

    @Override
    public ResponseEntity<Purchase> updatePurchase(Purchase purchase) {
        return null;
    }

    @Override
    public ResponseEntity<Purchase> deletePurchase(Long id) {
        return null;
    }

    @Override
    public ResponseEntity<List<Transaction>> retrieveAllTransactions() {
        return null;
    }

    @Override
    public ResponseEntity<Transaction> retrieveTransaction(Long id) {
        return null;
    }

    @Override
    public ResponseEntity<Transaction> saveTransaction(Transaction transaction) {
        return null;
    }

    @Override
    public ResponseEntity<Transaction> updateTransaction(Transaction transaction) {
        return null;
    }

    @Override
    public ResponseEntity<Transaction> deleteTransaction(Long id) {
        return null;
    }

    @Override
    public ResponseEntity<Purchase> grantProductToCustomer(String customerDocNumber, String productCategory) {
        return null;
    }

    @Override
    public ResponseEntity<List<Purchase>> retrieveCustomerPurchases(Long customerId) {
        return null;
    }

    @Override
    public ResponseEntity<Transaction> deposit(Long customerId, Long purchaseId, double amount) {
        return null;
    }

    @Override
    public ResponseEntity<Transaction> withdraw(Long customerId, Long purchaseId, double amount) {
        return null;
    }
}
