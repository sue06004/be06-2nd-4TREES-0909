package org.example.fourtreesproject.bid.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.fourtreesproject.bid.model.request.BidCancelRequest;
import org.example.fourtreesproject.bid.model.request.BidModifyRequest;
import org.example.fourtreesproject.bid.model.request.BidRegisterRequest;
import org.example.fourtreesproject.bid.model.response.BidRegisterResponse;
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
@Tag(name = "입찰 기능")
public class BidController {
    private final BidService bidService;

    @Operation(summary = "입찰 등록 api", description = "업체 회원이 등록된 대기 중인 공구에 입찰을 등록 <br><br>" +
            "※ 업체 회원 로그인이 필요한 기능입니다.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                @ExampleObject(name = "Valid example", value = """
                                    {
                                      "productIdx": 6,
                                      "gpbuyIdx": 6,
                                      "bidPrice": 30000
                                    }
                                """),
                                @ExampleObject(name = "Default", value = """
                               {
                                 "productIdx": 0,
                                 "gpbuyIdx": 0,
                                 "bidPrice": 0
                               }""")
                            }
                    )
            )
    )
    @PostMapping("/register")
    public BaseResponse<BidRegisterResponse> register(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody BidRegisterRequest bidRegisterRequest) {
        if (customUserDetails == null) throw new InvalidBidException(USER_AUTHENTICATION_FAILED);
        Long userIdx = customUserDetails.getIdx();
        return new BaseResponse<>(bidService.register(userIdx, bidRegisterRequest));
    }

    @Operation(summary = "입찰 현황 조회 api", description = "업체회원이 자신이 등록한 (선정 되었거나 선정이 되지 않은)입찰 리스트 조회 <br><br>" +
            "※ 업체 회원 로그인이 필요한 기능입니다.")
    @GetMapping("/mylist")
    public BaseResponse<String> myList(@AuthenticationPrincipal CustomUserDetails customUserDetails, Integer page, Integer size, Boolean bidSelect) {
        if (customUserDetails == null) throw new InvalidBidException(USER_AUTHENTICATION_FAILED);
        Long userIdx = customUserDetails.getIdx();
        return new BaseResponse(bidService.myList(page, size, userIdx, bidSelect));
    }

    @Operation(summary = "입찰 대기 공구 조회 api",
            description = "업체 회원이 카테고리(필수 x), 제목(필수 x)을 통해 업체가 입찰할 수 있는 공구 목록을 조회")
    @GetMapping("/gpbuy/status-wait/list")
    public BaseResponse<String> gpbuyWaitList(Integer page, Integer size,
                                              @RequestParam(required = false) Long categoryIdx,
                                              @RequestParam(required = false) String gpbuyTitle) {
        return new BaseResponse(bidService.statusWaitList(page, size, categoryIdx, gpbuyTitle));
    }


    @Operation(summary = "입찰 수정 api", description = "업체 회원이 등록한 입찰 수정 (단, 입찰 등록을 한 공구의 상태가 대기 중일 경우에만 가능) <br><br>" +
            "※ 업체 회원 로그인이 필요한 기능입니다.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                @ExampleObject(name = "Valid example", value = """
                               {
                                 "bidIdx": 22,
                                 "productIdx": 14,
                                 "bidPrice": 20000
                               }"""),
                                @ExampleObject(name = "Default", value = """
                               {
                                 "bidIdx": 0,
                                 "productIdx": 0,
                                 "bidPrice": 0
                               }""")
                            }
                    )
            )
    )
    @PostMapping("/modify")
    public BaseResponse<String> modify(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody BidModifyRequest bidModifyRequest) {
        if (customUserDetails == null) throw new InvalidBidException(USER_AUTHENTICATION_FAILED);
        Long userIdx = customUserDetails.getIdx();
        bidService.modify(userIdx, bidModifyRequest);
        return new BaseResponse<>();
    }

    @Operation(summary = "입찰 취소 api", description = "업체 회원이 등록한 입찰을 취소 (단, 입찰 등록을 한 공구의 상태가 대기 중일 경우에만 가능) <br><br>" +
            "※ 업체 회원 로그인이 필요한 기능입니다.")
    @PostMapping("/cancel")
    public BaseResponse<String> cancel(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody BidCancelRequest bidCancelRequest) {
        if (customUserDetails == null) throw new InvalidBidException(USER_AUTHENTICATION_FAILED);
        Long userIdx = customUserDetails.getIdx();
        bidService.cancel(userIdx, bidCancelRequest);
        return new BaseResponse<>();
    }
}
