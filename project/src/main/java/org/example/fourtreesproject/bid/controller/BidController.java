package org.example.fourtreesproject.bid.controller;

import lombok.RequiredArgsConstructor;
import org.example.fourtreesproject.bid.model.request.BidRegisterRequest;
import org.example.fourtreesproject.bid.service.BidService;
import org.example.fourtreesproject.common.BaseResponse;
import org.example.fourtreesproject.common.BaseResponseStatus;
import org.example.fourtreesproject.user.model.dto.CustomUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/bid")
@RequiredArgsConstructor
public class BidController {
    private final BidService bidService;

    // TODO : 예외처리
    @PostMapping("/register")
    public BaseResponse<String> register(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody BidRegisterRequest bidRegisterRequest) {
        if (customUserDetails == null) {

            return new BaseResponse<>("사용자 인증 실패");
        }
        Long userIdx = customUserDetails.getIdx();
        bidService.register(userIdx, bidRegisterRequest);
        return new BaseResponse<>();
    }

    // TODO : 예외처리
    @GetMapping("/mylist")
    public BaseResponse<String> myList(@AuthenticationPrincipal CustomUserDetails customUserDetails, Boolean bidSelect) {
        return new BaseResponse(bidService.myList(customUserDetails.getIdx(), bidSelect));
    }
}
