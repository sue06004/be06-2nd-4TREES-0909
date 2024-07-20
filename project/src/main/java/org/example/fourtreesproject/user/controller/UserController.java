package org.example.fourtreesproject.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.example.fourtreesproject.common.BaseResponse;
import org.example.fourtreesproject.companyRegVerify.service.CompanyRegVerifyService;
import org.example.fourtreesproject.delivery.model.request.DeliveryAddressRegisterRequest;
import org.example.fourtreesproject.emailVerify.model.dto.EmailVerifyDto;
import org.example.fourtreesproject.emailVerify.service.EmailVerifyService;
import org.example.fourtreesproject.exception.custom.InvalidUserException;
import org.example.fourtreesproject.user.model.dto.CustomUserDetails;
import org.example.fourtreesproject.user.model.request.SellerSignupRequest;
import org.example.fourtreesproject.user.model.request.UserSignupRequest;
import org.example.fourtreesproject.user.model.response.SellerInfoResponse;
import org.example.fourtreesproject.user.model.response.UserInfoResponse;
import org.example.fourtreesproject.user.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static org.example.fourtreesproject.common.BaseResponseStatus.*;


@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final EmailVerifyService emailVerifyService;
    private final CompanyRegVerifyService companyRegVerifyService;

    @Operation(summary = "일반 유저 회원 가입 api")
    @PostMapping("/user/signup")
    public BaseResponse<String> signup(@RequestBody UserSignupRequest userSignupRequest) throws RuntimeException{
        userService.signup(userSignupRequest);
        String uuid = userService.sendEmail(userSignupRequest.getEmail());
        emailVerifyService.save(EmailVerifyDto.builder()
                .email(userSignupRequest.getEmail())
                .uuid(uuid)
                .build());
        return new BaseResponse<>();
    }

    @Operation(summary = "업체 유저 회원 가입 api")
    @PostMapping("/seller/signup")
    public BaseResponse<String> sellerSignup(@RequestBody SellerSignupRequest sellerSignupRequest) throws RuntimeException {
        // 사업자등록번호 검증
        if(companyRegVerifyService.verify(sellerSignupRequest.getSellerRegNum(), sellerSignupRequest.getComUuid())) {
            String emailUuid = userService.sendEmail(sellerSignupRequest.getEmail());
            userService.sellerSignup(sellerSignupRequest);
            emailVerifyService.save(EmailVerifyDto.builder()
                    .email(sellerSignupRequest.getEmail())
                    .uuid(emailUuid)
                    .build());
            return new BaseResponse<>();
        } else {
            return new BaseResponse<>(USER_BUSINESS_NUMBER_AUTH_FAIL);
        }
    }

    @Operation(summary = "이메일 인증 api")
    @GetMapping("/user/verify")
    public BaseResponse<String> verify(String email, String uuid) throws RuntimeException{
        Boolean verify = emailVerifyService.verifyEmail(EmailVerifyDto.builder()
                .email(email)
                .uuid(uuid)
                .build());
        if (verify){
            userService.activeMember(email);
            return new BaseResponse<>();
        }
        return new BaseResponse<>(USER_EMAIL_AUTH_FAIL);
    }

    @Operation(summary = "배송지 등록 api")
    @PostMapping("/user/delivery/register")
    public BaseResponse<String> deliveryRegister(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                 @RequestBody DeliveryAddressRegisterRequest deliveryAddressRegisterRequest) throws RuntimeException{
        if (customUserDetails == null){
            return new BaseResponse<>(USER_NOT_LOGIN);
        }
        userService.registerDelivery(customUserDetails.getUser(), deliveryAddressRegisterRequest);
        return new BaseResponse<>();
    }

    @Operation(summary = "일반 유저 상세 정보 조회 api")
    @GetMapping("/user/info/detail")
    public BaseResponse<UserInfoResponse> userInfoRead(@AuthenticationPrincipal CustomUserDetails customUserDetails) throws RuntimeException{
        if (customUserDetails == null){
            throw new InvalidUserException(USER_NOT_LOGIN);
        }
        UserInfoResponse userInfoResponse = userService.getUserInfoDetail(customUserDetails.getIdx());
        return new BaseResponse<>(userInfoResponse);
    }

    @Operation(summary = "업체 유저 상세 정보 조회 api")
    @GetMapping("/seller/info/detail")
    public BaseResponse<SellerInfoResponse> sellerInfoRead(@AuthenticationPrincipal CustomUserDetails customUserDetails) throws RuntimeException{
        if (customUserDetails == null){
            throw new InvalidUserException(USER_NOT_LOGIN);
        }
        SellerInfoResponse sellerInfoResponse = userService.getSellerInfoDetail(customUserDetails.getIdx());
        return new BaseResponse<>(sellerInfoResponse);
    }
}
