package org.example.fourtreesproject.product.model.response;


import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class ProductMylistResponse {
    private String productName;
    private String productContent;
    private ProductCategoryResponse category;
    private List<ProductImgResponse> productImgList;
}
