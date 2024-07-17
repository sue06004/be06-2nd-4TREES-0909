package org.example.fourtreesproject.groupbuy.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.example.fourtreesproject.common.BaseResponse;
import org.example.fourtreesproject.groupbuy.model.request.GroupBuyCreateRequest;
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


    @GetMapping("/registered/bid/list")
    public BaseResponse<List<RegisteredBidListResponse>> registeredBidList(
            Long gpbuyIdx
    ){
        List<RegisteredBidListResponse> result = gpbuyService.findBidList(gpbuyIdx);

        return new BaseResponse<>(result);
    }

    @GetMapping("/list")
    public BaseResponse<List<GroupBuyListResponse>> list(
            Integer page, Integer size
    ){
        List<GroupBuyListResponse> result = gpbuyService.list(page, size);

        return new BaseResponse<>(result);
    }
}
