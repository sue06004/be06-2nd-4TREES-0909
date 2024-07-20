package org.example.fourtreesproject.bid.controller;

import io.swagger.v3.oas.annotations.Operation;
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

import static org.example.fourtreesproject.common.BaseResponseStatus.USER_AUTHENTICATION_FAILED;


@RestController
@RequestMapping("/bid")
@RequiredArgsConstructor
public class BidController {
    private final BidService bidService;

    @Operation(summary = "입찰 등록 api")
    @PostMapping("/register")
    public BaseResponse<String> register(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody BidRegisterRequest bidRegisterRequest) {
        if (customUserDetails == null) throw new InvalidBidException(USER_AUTHENTICATION_FAILED);
        Long userIdx = customUserDetails.getIdx();
        bidService.register(userIdx, bidRegisterRequest);
        return new BaseResponse<>();
    }

    @Operation(summary = "입찰 현황 조회 api")
    @GetMapping("/mylist")
    public BaseResponse<String> myList(@AuthenticationPrincipal CustomUserDetails customUserDetails, Boolean bidSelect) {
        if (customUserDetails == null) throw new InvalidBidException(USER_AUTHENTICATION_FAILED);
        Long userIdx = customUserDetails.getIdx();
        return new BaseResponse(bidService.myList(userIdx, bidSelect));
    }

    @Operation(summary = "입찰 대기 공구 조회 api")
    @GetMapping("/gpbuy/status-wait/list")
    public BaseResponse<String> gpbuyWaitList(Integer page, Integer size,
                                              @RequestParam(required = false) Long categoryIdx,
                                              @RequestParam(required = false) String gpbuyTitle) {
        return new BaseResponse(bidService.statusWaitList(page, size, categoryIdx, gpbuyTitle));
    }

    @Operation(summary = "입찰 수정 api")
    @PostMapping("/modify")
    public BaseResponse<String> modify(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody BidModifyRequest bidModifyRequest) {
        if (customUserDetails == null) throw new InvalidBidException(USER_AUTHENTICATION_FAILED);
        Long userIdx = customUserDetails.getIdx();
        bidService.modify(userIdx, bidModifyRequest);
        return new BaseResponse<>();
    }

    @Operation(summary = "입찰 취소 api")
    @PostMapping("/cancel")
    public BaseResponse<String> cancel(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody BidCancelRequest bidCancelRequest) {
        if (customUserDetails == null) throw new InvalidBidException(USER_AUTHENTICATION_FAILED);
        Long userIdx = customUserDetails.getIdx();
        bidService.cancel(userIdx, bidCancelRequest);
        return new BaseResponse<>();
    }
}
