package org.example.fourtreesproject.product.model.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ProductImgResponse {
    private Long idx;
    private String productImgUrl;
    private Integer productImgSequence;
}
