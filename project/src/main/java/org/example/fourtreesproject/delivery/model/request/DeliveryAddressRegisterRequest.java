package org.example.fourtreesproject.delivery.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeliveryAddressRegisterRequest {
    private String addressName;
    private String addressInfo;
    private Integer postCode;
    private Boolean addressDefault;
}
