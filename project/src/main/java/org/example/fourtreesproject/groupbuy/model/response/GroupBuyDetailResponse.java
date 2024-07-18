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
public class GroupBuyDetailResponse {
    private Long gpbuyIdx;
    private Long userIdx;
    private String productThumbnailImg;
    private List<String> productImgUrlList;
    private String productName;
    private String companyName;
    private Integer gpbuyRemainQuantity;
    private Integer gpbuyQuantity;
    private LocalDateTime gpbuyStartedAt;
    private LocalDateTime gpbuyEndedAt;
    private String duration;
}
