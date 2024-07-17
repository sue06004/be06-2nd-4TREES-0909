package org.example.fourtreesproject.groupbuy.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.example.fourtreesproject.common.BaseResponse;
import org.example.fourtreesproject.common.BaseResponseStatus;
import org.example.fourtreesproject.groupbuy.model.request.GroupBuyCreateRequest;
import org.example.fourtreesproject.groupbuy.model.response.GroupBuyDetailResponse;
import org.example.fourtreesproject.groupbuy.model.response.GroupBuyListResponse;
import org.example.fourtreesproject.groupbuy.model.response.RegisteredBidListResponse;
import org.example.fourtreesproject.groupbuy.service.GroupBuyService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/gpbuy")
public class GroupBuyController {

    private final GroupBuyService gpbuyService;

    @Operation(summary = "공구 등록 api")
    @PostMapping("/register")
    public BaseResponse register(
            @RequestBody GroupBuyCreateRequest request
            ){

        if (!gpbuyService.save(request)) {
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

    @Operation(summary = "공구 시작처리 api")
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
            Long gpbuyIdx, Long userIdx
    ){
        if (!gpbuyService.likesSave(gpbuyIdx,userIdx)){
            return new BaseResponse<>(BaseResponseStatus.GROUPBUY_LIKES_CREATE_FAIL);
        }
        return new BaseResponse();
    }
}
