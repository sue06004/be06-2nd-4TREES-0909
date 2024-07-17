package org.example.fourtreesproject.user.service;

import lombok.RequiredArgsConstructor;
import org.example.fourtreesproject.user.model.entity.SellerDetail;
import org.example.fourtreesproject.user.model.entity.User;
import org.example.fourtreesproject.user.model.entity.UserDetail;
import org.example.fourtreesproject.user.model.request.SellerSignupRequest;
import org.example.fourtreesproject.user.model.request.UserSignupRequest;
import org.example.fourtreesproject.user.repository.SellerDetailRepository;
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
    private final SellerDetailRepository sellerDetailRepository;
    private final UserDetailRepository userDetailRepository;
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

    public void sellerSignup(SellerSignupRequest sellerSignupRequest) {
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

}
