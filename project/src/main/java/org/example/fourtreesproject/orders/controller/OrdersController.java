package org.example.fourtreesproject.orders.controller;

import com.siot.IamportRestClient.exception.IamportResponseException;
import lombok.RequiredArgsConstructor;
import org.example.fourtreesproject.common.BaseResponse;
import org.example.fourtreesproject.orders.service.OrdersService;
import org.example.fourtreesproject.user.model.dto.CustomUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrdersController {

    private final OrdersService ordersService;

    @GetMapping("/register")
    public BaseResponse<String> orderRegister(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                              String impUid) throws IamportResponseException, IOException, RuntimeException {

        ordersService.registerOrder(customUserDetails,impUid);
        return new BaseResponse<>();
    }
}
