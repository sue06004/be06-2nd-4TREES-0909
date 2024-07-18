package org.example.fourtreesproject.groupbuy.model.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GroupBuySearchRequest {
    private String category;
    private Integer minPrice;
    private Integer maxPrice;
    private Integer page;
    private Integer size;
}

