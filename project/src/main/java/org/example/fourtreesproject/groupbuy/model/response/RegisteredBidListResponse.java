package org.example.fourtreesproject.groupbuy.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisteredBidListResponse {
    private Long bidIdx;
    private Long productIdx;
    private String productName;
    private String productThumbnailImg;
    private Integer bidPrice;
    private String companyName;

}
