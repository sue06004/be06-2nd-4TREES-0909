package org.example.fourtreesproject.bid.model.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BidMyListResponse {
    private String productName;
    private String productImgUrl;
    private Integer bidPrice;
}
