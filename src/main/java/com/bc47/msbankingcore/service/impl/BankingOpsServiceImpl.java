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
import java.util.Random;

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
        purchaseCreated.setState("active");
        purchaseCreated.setAccountNo(generateRandomAccountNumber());
        purchaseCreated.setBalance(0.0);
        purchaseCreated.setHasMaintenanceCommission(false);
        purchaseCreated.setMaintenanceCommissionPercentage(0.0);
        purchaseCreated.setHasMonthTransactionLimitQty(false);
        purchaseCreated.setMaxCreditsQuantityAllowed(100);
        purchaseCreated.setCustomerCurrentMonthTransactionsMade(0);

        return customersClient.get()
                .uri(CUSTOMERS_PATH + "/" + purchase.getCustomerId())
                .retrieve()
                .bodyToFlux(Customer.class)
                .next()
                .flatMap(c -> {
                    purchaseCreated.setCustomerId(c.getId());
                    purchaseCreated.setCustomerName(c.getName());
                    purchaseCreated.setCustomerType(c.getCustomerType());
                    if (c.getCustomerType().equalsIgnoreCase("PERSON"))
                        purchaseCreated.setMaxCreditsQuantityAllowed(1);
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
                            if (p.getProductCategory().equalsIgnoreCase("CUENTA_CORRIENTE")) {
                                purchaseCreated.setHasMaintenanceCommission(true);
                                purchaseCreated.setMaintenanceCommissionPercentage(0.35);
                            }
                            if (p.getProductCategory().equalsIgnoreCase("CUENTA_AHORRO")) {
                                purchaseCreated.setHasMonthTransactionLimitQty(true);
                                purchaseCreated.setMonthTransactionLimitQty(10);
                            }
                            if (p.getProductCategory().equalsIgnoreCase("CUENTA_PLAZO_FIJO")) {
                                purchaseCreated.setHasMonthTransactionLimitQty(true);
                                purchaseCreated.setMonthTransactionLimitQty(1);
                            }
                            if (p.getProductType().equalsIgnoreCase("ACTIVE")) {
                                purchaseCreated.setCreditAmountLimit(10000.0);
                                purchaseCreated.setBalance(10000.0);
                            }
                            if (p.getProductType().equalsIgnoreCase("PASIVE")) {
                                purchaseCreated.setCreditAmountLimit(0.0);
                            }
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
                    transaction.setSource("Banca Móvil");
                    transaction.setState("available");
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
                    transaction.setSource("Banca Móvil");
                    transaction.setState("available");
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
    public Flux<Transaction> retrieveCustomerPurchaseMovements(String customerId, String purchaseId) {
        return transactionsClient.get()
                .uri(TRANSACTIONS_PATH + "/" + customerId + "/" + purchaseId)
                .retrieve()
                .bodyToFlux(Transaction.class);
    }

    private String generateRandomAccountNumber() {
        StringBuilder start = new StringBuilder("19");
        Random value = new Random();

        int r1 = value.nextInt(10);
        int r2 = value.nextInt(10);
        start.append(r1).append(r2);

        int count = 0;
        int n = 0;
        for (int i = 0; i < 12; i++)
        {
            if(count == 4)
                count = 0;
            else
                n = value.nextInt(10);
            start.append(n);
            count++;
        }
        return start.toString();
    }
}
