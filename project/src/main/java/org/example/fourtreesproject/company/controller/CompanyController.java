package org.example.fourtreesproject.company.controller;

import lombok.RequiredArgsConstructor;
import org.example.fourtreesproject.common.BaseResponse;
import org.example.fourtreesproject.company.model.request.CompanyRegisterRequest;
import org.example.fourtreesproject.company.service.CompanyService;
import org.example.fourtreesproject.user.model.dto.CustomUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.management.relation.Role;
import java.util.Optional;

import static org.example.fourtreesproject.common.BaseResponseStatus.*;

@RestController
@RequiredArgsConstructor
@CrossOrigin("*")
@RequestMapping("/company")
public class CompanyController {
    private final CompanyService companyService;

    //업체 정보 등록
    @RequestMapping(method = RequestMethod.POST, value = "/register")
    public BaseResponse<String> register(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                         @RequestBody CompanyRegisterRequest request) {
        if (!customUserDetails.getAuthorities().equals("ROLE_USER")){
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

}
