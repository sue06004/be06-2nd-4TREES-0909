package org.example.fourtreesproject.company.service;

import lombok.RequiredArgsConstructor;
import org.example.fourtreesproject.company.model.entity.Company;
import org.example.fourtreesproject.company.model.request.CompanyModifyRequest;
import org.example.fourtreesproject.company.model.request.CompanyRegisterRequest;
import org.example.fourtreesproject.company.model.response.CompanyDetailResponse;
import org.example.fourtreesproject.company.model.response.CompanyRegisterResponse;
import org.example.fourtreesproject.company.repository.CompanyRepository;
import org.example.fourtreesproject.exception.custom.InvalidCompanyException;
import org.example.fourtreesproject.user.model.entity.User;
import org.springframework.stereotype.Service;

import static org.example.fourtreesproject.common.BaseResponseStatus.COMPANY_REGIST_FAIL;

@Service
@RequiredArgsConstructor
public class CompanyService {
    private final CompanyRepository companyRepository;

    //업체 정보 등록
    public CompanyRegisterResponse register(CompanyRegisterRequest request, User user) throws RuntimeException{
        if (companyRepository.findByUserIdx(user.getIdx()).isPresent()){
            throw new InvalidCompanyException(COMPANY_REGIST_FAIL);
        }
        Company registCompany = Company.builder()
                .user(user)
                .companyName(request.getCompanyName())
                .companyAddress(request.getCompanyAddress())
                .companyPostCode(request.getCompanyPostCode())
                .companyType(request.getCompanyType())
                .companyIntro(request.getCompanyIntro())
                .build();

       Company company = companyRepository.save(registCompany);

        return CompanyRegisterResponse.builder()
                .companyIdx(company.getIdx())
                .build();
    }

    //업체 정보 수정
    public void modify(CompanyModifyRequest request, User user) {
        Company company = companyRepository.findByUserIdx(user.getIdx()).orElseThrow(); //db의 업체정보 가져옴

        company = Company.builder()     //변경값이 없으면 company 데이터 그대로, 있으면 request 데이터로 변경
                .user(user)
                .idx(company.getIdx())
                .companyName((request.getCompanyName() == null) ? company.getCompanyName() : request.getCompanyName())
                .companyAddress((request.getCompanyAddress() == null) ? company.getCompanyAddress() : request.getCompanyAddress())
                .companyPostCode((request.getCompanyPostCode() == null) ? company.getCompanyPostCode() : request.getCompanyPostCode())
                .companyType((request.getCompanyType() == null) ? company.getCompanyType() : request.getCompanyType())
                .companyIntro((request.getCompanyIntro() == null) ? company.getCompanyIntro() : request.getCompanyIntro())
                .build();

        companyRepository.save(company);

    }

    //업체 정보 조회
    public CompanyDetailResponse detail(User user) {

        Company company = companyRepository.findByUserIdx(user.getIdx()).orElseThrow();
        CompanyDetailResponse detailResponse = CompanyDetailResponse.builder()
                .companyName(company.getCompanyName())
                .companyAddress(company.getCompanyAddress())
                .companyPostCode(company.getCompanyPostCode())
                .companyType(company.getCompanyType())
                .companyIntro(company.getCompanyIntro())
                .build();

        return detailResponse;
    }
}