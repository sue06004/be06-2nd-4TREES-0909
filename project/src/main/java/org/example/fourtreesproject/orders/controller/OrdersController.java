package org.example.fourtreesproject.orders.controller;

import com.siot.IamportRestClient.exception.IamportResponseException;
import com.sun.jna.platform.unix.X11;
import lombok.RequiredArgsConstructor;
import org.example.fourtreesproject.common.BaseResponse;
import org.example.fourtreesproject.orders.model.response.OrdersListResponse;
import org.example.fourtreesproject.orders.service.OrdersService;
import org.example.fourtreesproject.user.model.dto.CustomUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrdersController {

    private final OrdersService ordersService;

    @GetMapping("/register")
    public BaseResponse<String> orderRegister(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                              String impUid) throws IamportResponseException, IOException, RuntimeException {

        ordersService.registerOrder(customUserDetails.getIdx(),impUid);
        return new BaseResponse<>();
    }

    @GetMapping("/info/list")
    public BaseResponse<List<OrdersListResponse>> readOrderInfoList(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                                   Integer page, Integer size) throws RuntimeException {
        List<OrdersListResponse> orderInfoList = ordersService.getOrderInfoList(page, size, customUserDetails.getIdx());
        return new BaseResponse<>(orderInfoList);
    }
}
