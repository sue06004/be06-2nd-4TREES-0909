package org.example.fourtreesproject.user.service;

import lombok.RequiredArgsConstructor;
import org.example.fourtreesproject.delivery.model.DeliveryAddress;
import org.example.fourtreesproject.delivery.model.request.DeliveryAddressRegisterRequest;
import org.example.fourtreesproject.delivery.repository.DeliveryAddressRepository;
import org.example.fourtreesproject.user.model.entity.User;
import org.example.fourtreesproject.user.model.entity.UserDetail;
import org.example.fourtreesproject.user.model.request.UserSignupRequest;
import org.example.fourtreesproject.user.repository.UserDetailRepository;
import org.example.fourtreesproject.user.repository.UserRepository;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserDetailRepository userDetailRepository;
    private final DeliveryAddressRepository deliveryAddressRepository;

    private final JavaMailSender emailSender;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void signup(UserSignupRequest userSignupReq) {
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

    public String sendEmail(String email){
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(email); // 받는 사람 메일
        simpleMailMessage.setSubject("[내 사이트] 가입 환영"); // 메일 제목
        String uuid = UUID.randomUUID().toString();
        simpleMailMessage.setText("http://localhost:8080/user/verify?email="+email+"&uuid="+uuid); // 메일 내용

        emailSender.send(simpleMailMessage); // 메일 보내기
        return uuid;
    }

    public void activeMember(String email){
        Optional<User> userOptional = userRepository.findByEmail(email);
        if(userOptional.isPresent()){
            User user = userOptional.get();
            user.updateEmailStatus();
            user.updateStatus("활동");
            userRepository.save(user);
        }
    }

    public void registerDelivery(User user, DeliveryAddressRegisterRequest deliveryAddressRegisterRequest){
        DeliveryAddress defaultDelivery = deliveryAddressRepository.findByUserIdxAndAddressDefaultTrue(user.getIdx()).orElse(null);
        if(defaultDelivery != null){
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

}
