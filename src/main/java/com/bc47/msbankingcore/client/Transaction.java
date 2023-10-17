package com.bc47.msbankingcore.client;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Transaction {

    private Long id;
    private Long purchaseId;
    private String source;
    private String transactionType;
    private String createdAt;
    private Double amount;
    private String state;
}
