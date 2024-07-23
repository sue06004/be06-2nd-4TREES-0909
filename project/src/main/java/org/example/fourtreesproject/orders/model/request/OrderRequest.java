package org.example.fourtreesproject.orders.model.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OrderRequest {
    private String impUid;
    private Long gpBuyIdx;
    private Long couponIdx;
    private Long usePoint;
}
