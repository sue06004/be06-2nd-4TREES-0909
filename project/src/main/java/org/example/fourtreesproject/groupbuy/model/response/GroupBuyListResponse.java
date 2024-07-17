package org.example.fourtreesproject.groupbuy.model.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupBuyListResponse {
    private Long gpbuyIdx;
    private Integer gpbuyQuantity;
    private Integer gpbuyRemainQuantity;
    private String productThumbnailImg;
    private String productName;
    private Integer bidPrice;
    private String companyName;
}
