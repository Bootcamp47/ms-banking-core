package com.bc47.msbankingcore.client;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Product {

    private Long id;
    private String productType;
    private String productCategory;
    private String state;
    private String createdAt;
}
