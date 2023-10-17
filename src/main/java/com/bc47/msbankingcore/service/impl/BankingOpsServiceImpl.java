package com.bc47.msbankingcore.service.impl;

import com.bc47.msbankingcore.client.Customer;
import com.bc47.msbankingcore.client.Product;
import com.bc47.msbankingcore.client.Purchase;
import com.bc47.msbankingcore.client.Transaction;
import com.bc47.msbankingcore.service.BankingOpsService;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.Objects;
import static com.bc47.msbankingcore.util.Constants.*;

@Service
public class BankingOpsServiceImpl implements BankingOpsService {

    private final WebClient customersClient;
    private final WebClient productsClient;
    private final WebClient purchasesClient;
    private final WebClient transactionsClient;

    public BankingOpsServiceImpl(WebClient.Builder webClientBuilder) {
        this.customersClient = webClientBuilder.baseUrl(CUSTOMERS_URI).build();
        this.productsClient = webClientBuilder.baseUrl(PRODUCTS_URI).build();
        this.purchasesClient = webClientBuilder.baseUrl(PURCHASES_URI).build();
        this.transactionsClient = webClientBuilder.baseUrl(TRANSACTIONS_URI).build();
    }

    @Override
    public Flux<Purchase> grantProductToCustomer(Purchase purchase) {
        Purchase purchaseCreated = new Purchase();

        return customersClient.get()
                .uri(CUSTOMERS_PATH + "/" + purchase.getCustomerId())
                .retrieve()
                .bodyToFlux(Customer.class)
                .next()
                .flatMap(c -> {
                    purchaseCreated.setCustomerId(c.getId());
                    purchaseCreated.setCustomerName(c.getName());
                    purchaseCreated.setCustomerType(c.getCustomerType());
                    return Mono.just(purchaseCreated);
                })
                .concatWith(
                        productsClient.get()
                        .uri(PRODUCTS_PATH + "/" + purchase.getProductId())
                        .retrieve()
                        .bodyToFlux(Product.class)
                        .next()
                        .flatMap(p -> {
                            purchaseCreated.setProductId(p.getId());
                            purchaseCreated.setProductType(p.getProductType());
                            purchaseCreated.setProductCategory(p.getProductCategory());
                            return purchasesClient.post()
                                    .uri(PURCHASES_PATH)
                                    .body(Mono.just(purchaseCreated), Purchase.class)
                                    .retrieve()
                                    .bodyToMono(Purchase.class);
                        }));
    }

    @Override
    public Flux<Purchase> retrieveCustomerPurchases(String customerId) {
        return purchasesClient.get()
                .uri(PURCHASES_PATH + "/customer/" + customerId)
                .retrieve()
                .bodyToFlux(Purchase.class);
    }

    @Override
    public Flux<Transaction> deposit(String customerId, String purchaseId, double amount) {
        Transaction transaction = new Transaction();

        return customersClient.get()
                .uri(CUSTOMERS_PATH + "/" + customerId)
                .retrieve()
                .bodyToFlux(Customer.class)
                .next()
                .flatMap(customer -> {
                    transaction.setCustomerId(customer.getId());
                    transaction.setTransactionType("DEPOSIT");
                    transaction.setAmount(amount);
                    return Mono.just(transaction);
                })
                .concatWith(purchasesClient.get()
                        .uri(PURCHASES_PATH + "/customer/" + customerId)
                        .retrieve()
                        .bodyToFlux(Purchase.class)
                        .filter(p -> Objects.equals(p.getId(), purchaseId))
                        .flatMap(purchase -> {
                            transaction.setPurchaseId(purchase.getId());
                            return transactionsClient.post()
                                    .uri(TRANSACTIONS_PATH)
                                    .body(Mono.just(transaction), Transaction.class)
                                    .retrieve()
                                    .bodyToMono(Transaction.class);
                        }));
    }

    @Override
    public Flux<Transaction> withdraw(String customerId, String purchaseId, double amount) {
        Transaction transaction = new Transaction();

        return customersClient.get()
                .uri(CUSTOMERS_PATH + "/" + customerId)
                .retrieve()
                .bodyToFlux(Customer.class)
                .next()
                .flatMap(customer -> {
                    transaction.setCustomerId(customer.getId());
                    transaction.setTransactionType("WITHDRAWAL");
                    transaction.setAmount(amount);
                    return Mono.just(transaction);
                })
                .concatWith(purchasesClient.get()
                        .uri(PURCHASES_PATH + "/customer/" + customerId)
                        .retrieve()
                        .bodyToFlux(Purchase.class)
                        .filter(p -> Objects.equals(p.getId(), purchaseId))
                        .flatMap(purchase -> {
                            transaction.setPurchaseId(purchase.getId());
                            return transactionsClient.post()
                                    .uri(TRANSACTIONS_PATH)
                                    .body(Mono.just(transaction), Transaction.class)
                                    .retrieve()
                                    .bodyToMono(Transaction.class);
                        }));
    }
}
