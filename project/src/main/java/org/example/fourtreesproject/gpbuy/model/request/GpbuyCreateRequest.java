package org.example.fourtreesproject.gpbuy.model.request;


import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GpbuyCreateRequest {

    private Long userIdx;
    private String category;
    private String title;
    private String content;
    private Integer gpbuyQuantity;


}
