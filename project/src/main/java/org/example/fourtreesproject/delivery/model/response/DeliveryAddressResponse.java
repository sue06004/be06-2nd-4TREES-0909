package org.example.fourtreesproject.delivery.model.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class DeliveryAddressResponse {
    private String addressName;
    private String addressInfo;
    private Boolean addressDefault;
    private Integer postCode;
}
