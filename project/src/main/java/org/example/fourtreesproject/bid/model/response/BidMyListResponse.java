package org.example.fourtreesproject.bid.model.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BidMyListResponse {
    private Long bidIdx;
    private Long gpbuyIdx;
    private String gpbuyTitle;
    private String gpbuyStatus;
    private String productName;
    private String productImgUrl;
    private Integer bidPrice;
}
