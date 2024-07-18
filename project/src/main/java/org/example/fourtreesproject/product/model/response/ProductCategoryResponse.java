package org.example.fourtreesproject.product.model.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ProductCategoryResponse {
    private Long idx;
    private String categoryName;
}
