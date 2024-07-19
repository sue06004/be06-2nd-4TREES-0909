package org.example.fourtreesproject.bid.controller;

import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.example.fourtreesproject.bid.model.request.BidCancelRequest;
import org.example.fourtreesproject.bid.model.request.BidModifyRequest;
import org.example.fourtreesproject.bid.model.request.BidRegisterRequest;
import org.example.fourtreesproject.bid.service.BidService;
import org.example.fourtreesproject.common.BaseResponse;
import org.example.fourtreesproject.exception.custom.InvalidBidException;
import org.example.fourtreesproject.user.model.dto.CustomUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static org.example.fourtreesproject.common.BaseResponseStatus.*;


@RestController
@RequestMapping("/bid")
@RequiredArgsConstructor
public class BidController {
    private final BidService bidService;

    @PostMapping("/register")
    public BaseResponse<String> register(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody BidRegisterRequest bidRegisterRequest) {
        if (customUserDetails == null) throw new InvalidBidException(USER_AUTHENTICATION_FAILED);
        Long userIdx = customUserDetails.getIdx();
        bidService.register(userIdx, bidRegisterRequest);
        return new BaseResponse<>();
    }

    @GetMapping("/mylist")
    public BaseResponse<String> myList(@AuthenticationPrincipal CustomUserDetails customUserDetails, Boolean bidSelect) {
        if (customUserDetails == null) throw new InvalidBidException(USER_AUTHENTICATION_FAILED);
        Long userIdx = customUserDetails.getIdx();
        return new BaseResponse(bidService.myList(userIdx, bidSelect));
    }

    @GetMapping("/gpbuy/status-wait/list")
    public BaseResponse<String> gpbuyWaitList(Integer page, Integer size, Long categoryIdx, String gpbuyTitle) {
        return new BaseResponse(bidService.statusWaitList(page, size, categoryIdx, gpbuyTitle));
    }

    @PostMapping("/modify")
    public BaseResponse<String> modify(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody BidModifyRequest bidModifyRequest) {
        if (customUserDetails == null) throw new InvalidBidException(USER_AUTHENTICATION_FAILED);
        Long userIdx = customUserDetails.getIdx();
        bidService.modify(userIdx, bidModifyRequest);
        return new BaseResponse<>();
    }

    @PostMapping("/cancel")
    public BaseResponse<String> cancel(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody BidCancelRequest bidCancelRequest) {
        if (customUserDetails == null) throw new InvalidBidException(USER_AUTHENTICATION_FAILED);
        Long userIdx = customUserDetails.getIdx();
        bidService.cancel(userIdx, bidCancelRequest);
        return new BaseResponse<>();
    }
}
