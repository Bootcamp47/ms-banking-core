package com.bc47.msbankingcore.client;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Transaction {

    private Long id;
    private Long purchaseId;
    private String source;
    private String transactionType;
    private String createdAt;
    private Double amount;
    private String state;
}
