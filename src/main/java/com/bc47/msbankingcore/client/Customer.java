package com.bc47.msbankingcore.client;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Customer {

    private Long id;
    private String customerType;
    private String name;
    private String docType;
    private String docNumber;
    private String createdAt;
    private String address;
    private String phoneNumber;
    private String status;
    private String email;
    private String mobilePhoneImeiNumber;
    private Integer ownedPasiveProductsQty;
    private Integer ownedActiveProductsQty;
}
