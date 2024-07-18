package org.example.fourtreesproject.companyRegVerify.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.example.fourtreesproject.common.BaseResponse;
import org.example.fourtreesproject.companyRegVerify.model.request.CompanyRegVerifyRequest;
import org.example.fourtreesproject.companyRegVerify.service.CompanyRegVerifyService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/company-reg")
@RequiredArgsConstructor
public class CompanyRegVerifyController {
    private final CompanyRegVerifyService companyRegVerifyService;

    @PostMapping("/verify")
    public BaseResponse verify(@RequestBody CompanyRegVerifyRequest companyRegVerifyRequest) {
        return companyRegVerifyService.verify(companyRegVerifyRequest);
    }
}
