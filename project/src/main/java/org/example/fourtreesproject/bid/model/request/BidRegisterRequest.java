package org.example.fourtreesproject.bid.model.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BidRegisterRequest {
    private Long productIdx;
    private Long gpbuyIdx;
    private Integer bidPrice;
}
