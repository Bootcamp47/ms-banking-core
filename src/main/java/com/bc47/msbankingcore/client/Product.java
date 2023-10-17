package com.bc47.msbankingcore.client;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Product {

    private Long id;
    private String productType;
    private String productCategory;
    private String state;
    private String createdAt;
}
