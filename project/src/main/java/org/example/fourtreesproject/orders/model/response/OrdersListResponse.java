package org.example.fourtreesproject.orders.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class OrdersListResponse {
    private Long groupBuyIdx;
    private String groupBuyStatus;
    private String productName;
    private String deliveryNumber;

}
