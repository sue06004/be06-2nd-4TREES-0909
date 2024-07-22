package org.example.fourtreesproject.user.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.fourtreesproject.coupon.model.Coupon;
import org.example.fourtreesproject.coupon.model.UserCoupon;
import org.example.fourtreesproject.coupon.model.response.CouponResponse;
import org.example.fourtreesproject.delivery.model.DeliveryAddress;
import org.example.fourtreesproject.delivery.model.request.DeliveryAddressRegisterRequest;
import org.example.fourtreesproject.delivery.model.response.DeliveryAddressResponse;
import org.example.fourtreesproject.delivery.repository.DeliveryAddressRepository;
import org.example.fourtreesproject.exception.custom.InvalidUserException;
import org.example.fourtreesproject.user.model.entity.SellerDetail;
import org.example.fourtreesproject.user.model.entity.User;
import org.example.fourtreesproject.user.model.entity.UserDetail;
import org.example.fourtreesproject.user.model.request.SellerSignupRequest;
import org.example.fourtreesproject.user.model.request.UserSignupRequest;
import org.example.fourtreesproject.user.model.response.SellerInfoResponse;
import org.example.fourtreesproject.user.model.response.UserInfoResponse;
import org.example.fourtreesproject.user.repository.SellerDetailRepository;
import org.example.fourtreesproject.user.repository.UserDetailRepository;
import org.example.fourtreesproject.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.example.fourtreesproject.common.BaseResponseStatus.USER_INFO_DETAIL_FAIL;
import static org.example.fourtreesproject.common.BaseResponseStatus.USER_REGISTER_FAIL_EMAIL_DUPLICATION;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final SellerDetailRepository sellerDetailRepository;
    private final UserDetailRepository userDetailRepository;
    private final DeliveryAddressRepository deliveryAddressRepository;

    private final JavaMailSender emailSender;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Value("${project.mail.url}")
    private String mailUrl;

    @Transactional
    public void signup(UserSignupRequest userSignupReq) throws RuntimeException {
        checkDuplicateEmail(userSignupReq.getEmail());
        User user = User.builder()
                .type("inapp")
                .email(userSignupReq.getEmail())
                .password(bCryptPasswordEncoder.encode(userSignupReq.getPassword()))
                .name(userSignupReq.getName())
                .phoneNumber(userSignupReq.getPhoneNumber())
                .birth(userSignupReq.getBirth())
                .sex(userSignupReq.getSex())
                .address(userSignupReq.getAddress())
                .postCode(userSignupReq.getPostCode())
                .build();
        UserDetail userDetail = UserDetail.builder().user(user).build();
        userRepository.save(user);
        userDetailRepository.save(userDetail);
    }

    private void checkDuplicateEmail(String email) throws RuntimeException{
        User existingUser = userRepository.findByEmail(email).orElse(null);
        if (existingUser != null) {
            throw new InvalidUserException(USER_REGISTER_FAIL_EMAIL_DUPLICATION);
        }
    }

    @Transactional
    public void sellerSignup(SellerSignupRequest sellerSignupRequest) throws RuntimeException {
        checkDuplicateEmail(sellerSignupRequest.getEmail());
        User user = User.builder()
                .type("inapp")
                .role("ROLE_SELLER")
                .email(sellerSignupRequest.getEmail())
                .password(bCryptPasswordEncoder.encode(sellerSignupRequest.getPassword()))
                .name(sellerSignupRequest.getName())
                .phoneNumber(sellerSignupRequest.getPhoneNumber())
                .birth(sellerSignupRequest.getBirth())
                .sex(sellerSignupRequest.getSex())
                .address(sellerSignupRequest.getAddress())
                .postCode(sellerSignupRequest.getPostCode())
                .build();
        SellerDetail sellerDetail = SellerDetail.builder()
                .sellerAccount(sellerSignupRequest.getSellerAccount())
                .sellerBank(sellerSignupRequest.getSellerBank())
                .sellerOpenedAt(sellerSignupRequest.getSellerOpenedAt())
                .sellerMosNum(sellerSignupRequest.getSellerMosNum())
                .sellerRegNum(sellerSignupRequest.getSellerRegNum())
                .sellerDepoName(sellerSignupRequest.getSellerDepoName())
                .user(user)
                .build();
        userRepository.save(user);
        sellerDetailRepository.save(sellerDetail);
    }

    public String sendEmail(String email) throws RuntimeException {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(email); // 받는 사람 메일
        simpleMailMessage.setSubject("[내 사이트] 가입 환영"); // 메일 제목
        String uuid = UUID.randomUUID().toString();
        simpleMailMessage.setText(mailUrl+"/user/verify?email=" + email + "&uuid=" + uuid); // 메일 내용

        emailSender.send(simpleMailMessage); // 메일 보내기
        return uuid;
    }

    public void activeMember(String email) throws RuntimeException {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new InvalidUserException(USER_INFO_DETAIL_FAIL));
        user.updateEmailStatus();
        user.updateStatus("활동");
        userRepository.save(user);
    }

    @Transactional
    public void registerDelivery(User user, DeliveryAddressRegisterRequest deliveryAddressRegisterRequest) throws RuntimeException {
        DeliveryAddress defaultDelivery = deliveryAddressRepository.findByUserIdxAndAddressDefaultTrue(user.getIdx()).orElse(null);
        if (defaultDelivery != null && deliveryAddressRegisterRequest.getAddressDefault()) {
            defaultDelivery.updateDefault();
            deliveryAddressRepository.save(defaultDelivery);
        }

        DeliveryAddress deliveryAddress = DeliveryAddress.builder()
                .addressDefault(deliveryAddressRegisterRequest.getAddressDefault())
                .addressName(deliveryAddressRegisterRequest.getAddressName())
                .addressInfo(deliveryAddressRegisterRequest.getAddressInfo())
                .postCode(deliveryAddressRegisterRequest.getPostCode())
                .user(user)
                .build();
        deliveryAddressRepository.save(deliveryAddress);
    }

    public UserInfoResponse getUserInfoDetail(Long userIdx) throws RuntimeException {
        User user = userRepository.findUserInfoDetail(userIdx).orElseThrow(() -> new InvalidUserException(USER_INFO_DETAIL_FAIL));
        UserDetail userDetail = userDetailRepository.findByUserIdx(userIdx).orElseThrow(() -> new InvalidUserException(USER_INFO_DETAIL_FAIL));
        List<DeliveryAddressResponse> deliveryAddressResponseList = getDeliveryAddressResponseList(user);
        List<CouponResponse> couponResponseList = getCouponResponseList(user);
        return UserInfoResponse.builder()
                .address(user.getAddress())
                .postCode(user.getPostCode())
                .sex(user.getSex())
                .birth(user.getBirth())
                .name(user.getName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .deliveryAddressList(deliveryAddressResponseList)
                .couponList(couponResponseList)
                .userPoint(userDetail.getPoint())
                .build();
    }

    private List<CouponResponse> getCouponResponseList(User user) {
        List<CouponResponse> couponResponseList = new ArrayList<>();
        for (UserCoupon userCoupon : user.getUserCouponList()) {
            if (!userCoupon.getCouponStatus()) {
                continue;
            }
            Coupon coupon = userCoupon.getCoupon();
            CouponResponse couponResponse = CouponResponse.builder()
                    .couponContent(coupon.getCouponContent())
                    .couponName(coupon.getCouponName())
                    .couponPrice(coupon.getCouponPrice())
                    .minOrderPrice(coupon.getMinOrderPrice())
                    .build();
            couponResponseList.add(couponResponse);
        }
        return couponResponseList;
    }

    private List<DeliveryAddressResponse> getDeliveryAddressResponseList(User user) {
        List<DeliveryAddressResponse> deliveryAddressResponseList = new ArrayList<>();
        for (DeliveryAddress deliveryAddress : user.getDeliveryAddress()) {
            DeliveryAddressResponse deliveryAddressResponse = DeliveryAddressResponse.builder()
                    .addressDefault(deliveryAddress.getAddressDefault())
                    .addressName(deliveryAddress.getAddressName())
                    .addressInfo(deliveryAddress.getAddressInfo())
                    .postCode(deliveryAddress.getPostCode())
                    .build();
            deliveryAddressResponseList.add(deliveryAddressResponse);
        }
        return deliveryAddressResponseList;
    }


    public SellerInfoResponse getSellerInfoDetail(Long userIdx) throws RuntimeException {
        User seller = userRepository.findById(userIdx).orElseThrow(() -> new InvalidUserException(USER_INFO_DETAIL_FAIL));
        SellerDetail sellerDetail = sellerDetailRepository.findByUserIdx(seller.getIdx()).orElseThrow(() -> new InvalidUserException(USER_INFO_DETAIL_FAIL));
//        SellerDetail sellerDetail = seller.getSellerDetail();
        return SellerInfoResponse.builder()
                .address(seller.getAddress())
                .postCode(seller.getPostCode())
                .sex(seller.getSex())
                .birth(seller.getBirth())
                .name(seller.getName())
                .email(seller.getEmail())
                .phoneNumber(seller.getPhoneNumber())
                .sellerAccount(sellerDetail.getSellerAccount())
                .sellerBank(sellerDetail.getSellerBank())
                .sellerDepoName(sellerDetail.getSellerDepoName())
                .sellerMosNum(sellerDetail.getSellerMosNum())
                .sellerRegNum(sellerDetail.getSellerRegNum())
                .sellerOpenedAt(sellerDetail.getSellerOpenedAt())
                .build();

    }
}
