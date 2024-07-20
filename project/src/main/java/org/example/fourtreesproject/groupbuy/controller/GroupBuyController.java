package org.example.fourtreesproject.groupbuy.controller;

import com.siot.IamportRestClient.exception.IamportResponseException;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.example.fourtreesproject.common.BaseResponse;
import org.example.fourtreesproject.common.BaseResponseStatus;
import org.example.fourtreesproject.groupbuy.model.request.GroupBuyCreateRequest;
import org.example.fourtreesproject.groupbuy.model.request.GroupBuySearchRequest;
import org.example.fourtreesproject.groupbuy.model.response.GroupBuyDetailResponse;
import org.example.fourtreesproject.groupbuy.model.response.GroupBuyLikesListResponse;
import org.example.fourtreesproject.groupbuy.model.response.GroupBuyListResponse;
import org.example.fourtreesproject.groupbuy.model.response.RegisteredBidListResponse;
import org.example.fourtreesproject.groupbuy.service.GroupBuyService;
import org.example.fourtreesproject.user.model.dto.CustomUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

import static org.example.fourtreesproject.common.BaseResponseStatus.USER_NOT_LOGIN;

@RestController
@RequiredArgsConstructor
@RequestMapping("/gpbuy")
public class GroupBuyController {

    private final GroupBuyService gpbuyService;

    @Operation(summary = "공구 등록 api")
    @PostMapping("/register")
    public BaseResponse register(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestBody GroupBuyCreateRequest request
            ){
        if (customUserDetails == null){
            return new BaseResponse<>(USER_NOT_LOGIN);
        }

        if (!gpbuyService.save(customUserDetails.getUser().getIdx(),request)) {
            return new BaseResponse();
        }
        return new BaseResponse();
    }

    @Operation(summary = "공구에 입찰한 입찰정보 조회 api")
    @GetMapping("/registered/bid/list")
    public BaseResponse<List<RegisteredBidListResponse>> registeredBidList(
            Long gpbuyIdx
    ){
        List<RegisteredBidListResponse> result = gpbuyService.findBidList(gpbuyIdx);

        return new BaseResponse<>(result);
    }

    @GetMapping("/start")
    public BaseResponse start(
            Long gpbuyIdx
    ){
        if (!gpbuyService.start(gpbuyIdx)){
            return new BaseResponse(BaseResponseStatus.GROUPBUY_START_FAIL);
        }
        return new BaseResponse();
    }

    @Operation(summary = "공구 전체 조회 api")
    @GetMapping("/list")
    public BaseResponse<List<GroupBuyListResponse>> list(
            Integer page, Integer size
    ){
        List<GroupBuyListResponse> result = gpbuyService.list(page, size);

        return new BaseResponse<>(result);
    }

    @Operation(summary = "공구 상세 조회 api")
    @GetMapping("/detail")
    public BaseResponse<GroupBuyDetailResponse> detail(
            Long gpbuyIdx
    ){
        GroupBuyDetailResponse result = gpbuyService.findByGpbuyIdx(gpbuyIdx);

        return new BaseResponse<>(result);

    }
    @Operation(summary = "관심 공구 등록/취소 api")
    @GetMapping("/likes/save")
    public BaseResponse likesSave(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            Long gpbuyIdx
    ){
        if (customUserDetails == null){
            return new BaseResponse<>(USER_NOT_LOGIN);
        }
        if (!gpbuyService.likesSave(gpbuyIdx, customUserDetails.getIdx())){
            return new BaseResponse<>(BaseResponseStatus.GROUPBUY_LIKES_CREATE_FAIL);
        }
        return new BaseResponse();
    }

    //todo: list가 비어있으면 응답에서 result빼기
    @Operation(summary = "관심 공구 목록 조회 api")
    @GetMapping("/likes/list")
    public BaseResponse likesList(
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ){
        if (customUserDetails == null){
            return new BaseResponse<>(USER_NOT_LOGIN);
        }
        List<GroupBuyLikesListResponse> result = gpbuyService.likesList(customUserDetails.getIdx());
        return new BaseResponse(result);
    }

    @Operation(summary = "공구 검색 api")
    @GetMapping("/search")
    public BaseResponse search(
            GroupBuySearchRequest request
    ){
        List<GroupBuyListResponse> result = gpbuyService.search(request);

        return new BaseResponse<>(result);
    }

    @Operation(summary = "공구 취소 api")
    @GetMapping("/cancle")
    public BaseResponse cancle(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            Long ordersIdx
    ) throws IamportResponseException, IOException {
        if (gpbuyService.cancle(customUserDetails.getIdx(), ordersIdx)){
            return new BaseResponse();
        };

        //Todo: 캔슬 리스폰스 작성
        return new BaseResponse();
    }

}
