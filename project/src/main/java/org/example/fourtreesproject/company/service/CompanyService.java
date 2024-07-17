package org.example.fourtreesproject.company.service;

import lombok.RequiredArgsConstructor;
import org.example.fourtreesproject.company.model.entity.Company;
import org.example.fourtreesproject.company.model.request.CompanyRegisterRequest;
import org.example.fourtreesproject.company.repository.CompanyRepository;
import org.example.fourtreesproject.user.model.entity.User;
import org.example.fourtreesproject.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CompanyService {
    private final CompanyRepository companyRepository;

    //업체 정보 등록
    public void register(CompanyRegisterRequest request, User user) {

        Company registCompany = Company.builder()
                .user(user)
                .companyName(request.getCompanyName())
                .companyAddress(request.getCompanyAddress())
                .companyPostCode(request.getCompanyPostCode())
                .companyType(request.getCompanyType())
                .companyInfo(request.getCompanyIntro())
                .build();

        companyRepository.save(registCompany);


    }
}