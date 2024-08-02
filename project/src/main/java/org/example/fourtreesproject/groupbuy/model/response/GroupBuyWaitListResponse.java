package org.example.fourtreesproject.groupbuy.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupBuyWaitListResponse {
    private Long gpbuyIdx;
    private String gpbuyTitle;
    private LocalDateTime gpbuyRegedAt;
    private LocalDateTime gpbuyBidEndedAt;
}
