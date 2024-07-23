package org.example.fourtreesproject.bid.model.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GpbuyWaitListResponse {
    private Long gpbuyIdx;
    private String gpbuyTitle;
    private Integer gpbuyQuantity;
}
