package org.example.fourtreesproject.companyRegVerify.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
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

    @Operation(summary = "사업자등록번호 인증 api",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                @ExampleObject(name = "Valid example", value = """
                               {
                                 "b_no": "1208800767",
                                 "p_nm": "강한승",
                                 "start_dt": "20130716"
                               }"""),
                                @ExampleObject(name = "Invalid email", value = """
                               {
                                 "b_no": "1208800768",
                                 "p_nm": "서재은",
                                 "start_dt": "20130717"
                               }"""),
                                @ExampleObject(name = "Default", value = """
                               {
                                 "nickname": "string",
                                 "email": "string",
                                 "password": "string"
                               }""")
                            }
                    )
            )
    )
    @PostMapping("/verify")
    public BaseResponse verify(@RequestBody CompanyRegVerifyRequest companyRegVerifyRequest) {
        return companyRegVerifyService.validate(companyRegVerifyRequest);
    }
}
