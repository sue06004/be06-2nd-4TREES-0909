package org.example.fourtreesproject.user.service;

import lombok.RequiredArgsConstructor;
import org.example.fourtreesproject.coupon.model.Coupon;
import org.example.fourtreesproject.coupon.model.UserCoupon;
import org.example.fourtreesproject.coupon.model.response.CouponResponse;
import org.example.fourtreesproject.delivery.model.DeliveryAddress;
import org.example.fourtreesproject.delivery.model.request.DeliveryAddressRegisterRequest;
import org.example.fourtreesproject.delivery.model.response.DeliveryAddressResponse;
import org.example.fourtreesproject.delivery.repository.DeliveryAddressRepository;
import org.example.fourtreesproject.user.exception.custom.InvalidUserException;
import org.example.fourtreesproject.user.model.entity.SellerDetail;
import org.example.fourtreesproject.user.model.entity.User;
import org.example.fourtreesproject.user.model.entity.UserDetail;
import org.example.fourtreesproject.user.model.request.SellerSignupRequest;
import org.example.fourtreesproject.user.model.request.UserSignupRequest;
import org.example.fourtreesproject.user.model.response.UserInfoResponse;
import org.example.fourtreesproject.user.repository.SellerDetailRepository;
import org.example.fourtreesproject.user.repository.UserDetailRepository;
import org.example.fourtreesproject.user.repository.UserRepository;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.example.fourtreesproject.common.BaseResponseStatus.USER_INFO_DETAIL_FAIL;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final SellerDetailRepository sellerDetailRepository;
    private final UserDetailRepository userDetailRepository;
    private final DeliveryAddressRepository deliveryAddressRepository;

    private final JavaMailSender emailSender;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void signup(UserSignupRequest userSignupReq) throws Exception {
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

    public void sellerSignup(SellerSignupRequest sellerSignupRequest) throws Exception {
        User user = User.builder()
                .type("inapp")
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

    public String sendEmail(String email) throws Exception {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(email); // 받는 사람 메일
        simpleMailMessage.setSubject("[내 사이트] 가입 환영"); // 메일 제목
        String uuid = UUID.randomUUID().toString();
        simpleMailMessage.setText("http://localhost:8080/user/verify?email=" + email + "&uuid=" + uuid); // 메일 내용

        emailSender.send(simpleMailMessage); // 메일 보내기
        return uuid;
    }

    public void activeMember(String email) throws Exception {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.updateEmailStatus();
            user.updateStatus("활동");
            userRepository.save(user);
        }
    }

    public void registerDelivery(User user, DeliveryAddressRegisterRequest deliveryAddressRegisterRequest) throws Exception {
        DeliveryAddress defaultDelivery = deliveryAddressRepository.findByUserIdxAndAddressDefaultTrue(user.getIdx()).orElse(null);
        if (defaultDelivery != null) {
            defaultDelivery.updateDefault();
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

    public UserInfoResponse getUserInfoDetail(Long userIdx) throws Exception {
        User user = userRepository.findById(userIdx).orElse(null);
        if (user == null) {
            throw new InvalidUserException(USER_INFO_DETAIL_FAIL);
        }
        UserDetail userDetails = user.getUserDetail();
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
                .userPoint(userDetails.getPoint())
                .build();
    }

    private List<CouponResponse> getCouponResponseList(User user) {
        List<CouponResponse> couponResponseList = new ArrayList<>();
        for (UserCoupon userCoupon : user.getUserCouponList()) {
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
        for (DeliveryAddress deliveryAddress: user.getDeliveryAddress()) {
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

}
