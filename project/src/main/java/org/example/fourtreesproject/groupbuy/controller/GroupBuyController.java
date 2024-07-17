package org.example.fourtreesproject.groupbuy.controller;

import lombok.RequiredArgsConstructor;
import org.example.fourtreesproject.common.BaseResponse;
import org.example.fourtreesproject.groupbuy.model.request.GroupBuyCreateRequest;
import org.example.fourtreesproject.groupbuy.service.GroupBuyService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/gpbuy")
public class GroupBuyController {
    private final GroupBuyService gpbuyService;

//    @PostMapping("/register")
//    public BaseResponse register(
//            @RequestBody GroupBuyCreateRequest request
//            ){
//
//        if (gpbuyService.save(request)){
//            return new BaseResponse();
//        }
//
//    }
}
