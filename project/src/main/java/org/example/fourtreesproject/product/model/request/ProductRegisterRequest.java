package org.example.fourtreesproject.product.model.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

@Builder
@Getter
@Setter
public class ProductRegisterRequest {
    @NotNull
    private String productName;
    @NotNull
    private String productContent;
    @NotNull
    private Long categoryIdx;
}
