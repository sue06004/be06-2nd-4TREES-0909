package org.example.fourtreesproject.user.controller;

import lombok.RequiredArgsConstructor;
import org.example.fourtreesproject.common.BaseResponse;
import org.example.fourtreesproject.delivery.model.request.DeliveryAddressRegisterRequest;
import org.example.fourtreesproject.emailVerify.model.dto.EmailVerifyDto;
import org.example.fourtreesproject.emailVerify.service.EmailVerifyService;
import org.example.fourtreesproject.user.model.dto.CustomUserDetails;
import org.example.fourtreesproject.user.model.request.UserSignupRequest;
import org.example.fourtreesproject.user.service.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static org.example.fourtreesproject.common.BaseResponseStatus.USER_EMAIL_AUTH_FAIL;
import static org.example.fourtreesproject.common.BaseResponseStatus.USER_INFO_DETAIL_FAIL;


@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final EmailVerifyService emailVerifyService;

    @PostMapping("/basic/signup")
    public BaseResponse<String> signup(@RequestBody UserSignupRequest userSignupRequest) {
        String uuid = userService.sendEmail(userSignupRequest.getEmail());
        userService.signup(userSignupRequest);
        emailVerifyService.save(EmailVerifyDto.builder()
                .email(userSignupRequest.getEmail())
                .uuid(uuid)
                .build());
        return new BaseResponse<>("");
    }

    @GetMapping("/verify")
    public BaseResponse<String> verify(String email, String uuid) {
        Boolean verify = emailVerifyService.verifyEmail(EmailVerifyDto.builder()
                .email(email)
                .uuid(uuid)
                .build());
        if (verify){
            userService.activeMember(email);
            return new BaseResponse<>("");
        }
        return new BaseResponse<>(USER_EMAIL_AUTH_FAIL);
    }

    @PostMapping("/delivery/register")
    public BaseResponse<String> deliveryRegister(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                 @RequestBody DeliveryAddressRegisterRequest deliveryAddressRegisterRequest){
        if (customUserDetails == null){
            return new BaseResponse<>(USER_INFO_DETAIL_FAIL);
        }
        userService.registerDelivery(customUserDetails.getUser(), deliveryAddressRegisterRequest);
        return new BaseResponse<>();
    }
}
