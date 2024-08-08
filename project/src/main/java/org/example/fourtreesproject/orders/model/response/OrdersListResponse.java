package org.example.fourtreesproject.orders.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
@AllArgsConstructor
public class OrdersListResponse {
    private Long groupBuyIdx;
    private String groupBuyStatus;
    private String productName;
    private String productThumbnailImg;
    private String deliveryNumber;
    private Integer bidPrice;
    private LocalDateTime orderStartedAt;
    private String orderStatus;
}
