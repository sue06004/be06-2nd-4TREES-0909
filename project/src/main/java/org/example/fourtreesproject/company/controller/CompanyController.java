package org.example.fourtreesproject.company.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.example.fourtreesproject.common.BaseResponse;
import org.example.fourtreesproject.company.model.request.CompanyModifyRequest;
import org.example.fourtreesproject.company.model.request.CompanyRegisterRequest;
import org.example.fourtreesproject.company.model.response.CompanyDetailResponse;
import org.example.fourtreesproject.company.service.CompanyService;
import org.example.fourtreesproject.user.model.dto.CustomUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static org.example.fourtreesproject.common.BaseResponseStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/company")
public class CompanyController {
    private final CompanyService companyService;

    @Operation(summary = "업체 정보 등록 api")
    @RequestMapping(method = RequestMethod.POST, value = "/register")
    public BaseResponse<String> register(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                         @RequestBody CompanyRegisterRequest request) throws RuntimeException{

        if (customUserDetails.getUser().getRole().equals("ROLE_USER")) {
            return new BaseResponse<>(COMPANY_REGIST_FAIL); //업체회원이 아니면 예외처리
        }

        if (request.getCompanyName() == null) {
            return new BaseResponse<>(COMPANY_REGIST_FAIL_NAME_EMPTY);
        }
        if (request.getCompanyIntro() == null) {
            return new BaseResponse<>(COMPANY_REGIST_FAIL_INTRO_EMPTY);
        }
        if (request.getCompanyAddress() == null) {
            return new BaseResponse<>(COMPANY_REGIST_FAIL_ADDR_EMPTY);
        }
        if (request.getCompanyType() == null) {
            return new BaseResponse<>(COMPANY_REGIST_FAIL_TYPE_EMPTY);
        }

        if (request.getCompanyPostCode() == null) {
            return new BaseResponse<>(USER_INFO_MODIFY_FAIL_POST_CODE);
        }

        companyService.register(request, customUserDetails.getUser());
        return new BaseResponse<>();
    }


    @Operation(summary = "업체 정보 조회 api")
    @RequestMapping(method = RequestMethod.GET, value = "/detail")
    public BaseResponse<CompanyDetailResponse> detail(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        if (customUserDetails.getUser().getRole().equals("ROLE_USER")) {
            return new BaseResponse<>(COMPANY_REGIST_DETAIL_FAIL);
        }
        CompanyDetailResponse detailResponse = companyService.detail(customUserDetails.getUser());
        return new BaseResponse<>(detailResponse);
    }
    
    @Operation(summary = "업체 정보 수정 api")
    @RequestMapping(method = RequestMethod.POST, value = "/modify")
    public BaseResponse<String> modify(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                       @RequestBody CompanyModifyRequest request) {
        if (customUserDetails.getUser().getRole().equals("ROLE_USER")) {
            return new BaseResponse<>(COMPANY_REGIST_FAIL); //업체회원이 아니면 예외처리
        }
        companyService.modify(request, customUserDetails.getUser());
        return new BaseResponse<>();

    }
}

