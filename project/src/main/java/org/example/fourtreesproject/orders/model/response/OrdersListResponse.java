package org.example.fourtreesproject.orders.model.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
public class OrdersListResponse {
    private Long groupBuyIdx;
    private String groupBuyStatus;
    private String productName;
    private String deliveryNumber;

}
