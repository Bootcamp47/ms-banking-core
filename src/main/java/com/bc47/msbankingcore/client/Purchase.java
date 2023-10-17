package com.bc47.msbankingcore.client;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Purchase {

    private String id;
    private String customerId;
    private String customerType;
    private String customerName;
    private String productId;
    private String productType;
    private String productCategory;
    private String createdAt;
    private String state;
    private String accountNo;
    private Double balance;
    private Boolean hasMaintenanceCommission;
    private Double maintenanceCommissionPercentage;
    private Boolean hasMonthTransactionLimitQty;
    private Integer monthTransactionLimitQty;
    private Integer maxCreditsQuantityAllowed;
    private Double creditAmountLimit;
    private Integer customerCurrentMonthTransactionsMade;
}
