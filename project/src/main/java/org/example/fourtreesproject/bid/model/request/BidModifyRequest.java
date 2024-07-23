package org.example.fourtreesproject.bid.model.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BidModifyRequest {
    private Long bidIdx;
    private Long productIdx;
    private Integer bidPrice;
}
