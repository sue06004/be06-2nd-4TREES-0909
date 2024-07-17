package org.example.fourtreesproject.bid.controller;

import lombok.RequiredArgsConstructor;
import org.example.fourtreesproject.bid.model.request.BidRegisterRequest;
import org.example.fourtreesproject.bid.service.BidService;
import org.example.fourtreesproject.common.BaseResponse;
import org.example.fourtreesproject.user.model.dto.CustomUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/bid")
@RequiredArgsConstructor
public class BidController {
    private final BidService bidService;

    @PostMapping("/register")
    public BaseResponse<String> register(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody BidRegisterRequest bidRegisterRequest) {
        bidService.register(customUserDetails.getIdx(), bidRegisterRequest);
        return new BaseResponse<>();
    }


}
