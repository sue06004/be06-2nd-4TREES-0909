package org.example.fourtreesproject.emailVerify.service;

import lombok.RequiredArgsConstructor;
import org.example.fourtreesproject.emailVerify.model.dto.EmailVerifyDto;
import org.example.fourtreesproject.emailVerify.model.entity.EmailVerify;
import org.example.fourtreesproject.emailVerify.repository.EmailVerifyRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmailVerifyService {
    private final EmailVerifyRepository emailVerifyRepository;

    public void save(EmailVerifyDto emailVerifyDto) {
        EmailVerify emailVerify = emailVerifyRepository.findByEmail(emailVerifyDto.getEmail()).orElse(null);
        if (emailVerify == null) {
            emailVerify = EmailVerify.builder()
                    .email(emailVerifyDto.getEmail())
                    .uuid(emailVerifyDto.getUuid())
                    .build();
        } else{
            emailVerify.updateEmailVerify(emailVerifyDto.getUuid());
        }

        emailVerifyRepository.save(emailVerify);
    }

    public Boolean verifyEmail(EmailVerifyDto emailVerifyDto) {
        Optional<EmailVerify> emailVerifyOptional = emailVerifyRepository.findByEmail(emailVerifyDto.getEmail());
        if (emailVerifyOptional.isPresent()) {
            EmailVerify emailVerify = emailVerifyOptional.get();
            return emailVerify.getUuid().equals(emailVerifyDto.getUuid());
        }
        return false;
    }

}
