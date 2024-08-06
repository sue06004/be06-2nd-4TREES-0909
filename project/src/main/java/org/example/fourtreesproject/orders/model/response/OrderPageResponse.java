package org.example.fourtreesproject.orders.model.response;

import lombok.Builder;
import lombok.Getter;
import org.example.fourtreesproject.delivery.model.response.DeliveryAddressResponse;
import org.example.fourtreesproject.user.model.response.UserCouponResponse;

import java.util.List;

@Getter
@Builder
public class OrderPageResponse {
    private String name;
    private String email;
    private String phoneNumber;
    private Integer point;
    private List<UserCouponResponse> userCouponResponseList;
    private List<DeliveryAddressResponse> deliveryAddressResponseList;
    private Integer bidPrice;
    private String productName;
    private Integer quantity;
    private String productThumbnailUrl;
    private Long bidIdx;
}
