package org.example.fourtreesproject.gpbuy.controller;

import lombok.RequiredArgsConstructor;
import org.example.fourtreesproject.common.BaseResponse;
import org.example.fourtreesproject.common.BaseResponseStatus;
import org.example.fourtreesproject.gpbuy.model.request.GpbuyCreateRequest;
import org.example.fourtreesproject.gpbuy.service.GpbuyService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/gpbuy")
public class GpbuyController {
    private final GpbuyService gpbuyService;

    @PostMapping("/register")
    public BaseResponse register(
            @RequestBody GpbuyCreateRequest request
            ){

        if (gpbuyService.save(request)){
            return new BaseResponse();
        };

    }
}
