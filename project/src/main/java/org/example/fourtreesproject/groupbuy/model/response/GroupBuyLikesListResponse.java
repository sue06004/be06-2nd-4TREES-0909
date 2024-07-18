package org.example.fourtreesproject.groupbuy.model.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class GroupBuyLikesListResponse {
    private Long gpbuyIdx;
    private Integer gpbuyQuantity;
    private Integer gpbuyRemainQuantity;
    private String productThumbnailImg;
    private String productName;
    private Integer bidPrice;
    private String companyName;
    private LocalDateTime gpbuyStartedAt;
    private LocalDateTime gpbuyEndedAt;

}
