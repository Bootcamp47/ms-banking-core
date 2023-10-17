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
import static com.bc47.msbankingcore.util.Constants.*;

@Service
public class BankingOpsServiceImpl implements BankingOpsService {

    private WebClient customersClient;
    private WebClient productsClient;
    private WebClient purchasesClient;
    private WebClient transactionsClient;

    @Override
    public Flux<Purchase> grantProductToCustomer(Purchase purchase) {
        Purchase purchaseCreated = new Purchase();
        customersClient = WebClient.create(CUSTOMERS_URI);
        productsClient = WebClient.create(PRODUCTS_URI);
        purchasesClient = WebClient.create(PURCHASES_URI);

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
        purchasesClient = WebClient.create(PURCHASES_URI);
        return purchasesClient.get()
                .uri(PURCHASES_PATH + "/customer/" + customerId)
                .retrieve()
                .bodyToFlux(Purchase.class);
    }

    @Override
    public Mono<Transaction> deposit(String customerId, String purchaseId, double amount) {
        return null;
    }

    @Override
    public Mono<Transaction> withdraw(String customerId, String purchaseId, double amount) {
        return null;
    }
}
