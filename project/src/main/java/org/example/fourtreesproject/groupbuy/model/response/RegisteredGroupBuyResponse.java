package org.example.fourtreesproject.groupbuy.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisteredGroupBuyResponse {
    private Long gpbuyIdx;
    private String gpbuyTitle;
    private String gpbuyContent;
    private Long categoryIdx;
    private Integer gpbuyQuantity;
    private LocalDateTime gpbuyBidEndedAt;
    private List<RegisteredBidListResponse> bidList;

}
