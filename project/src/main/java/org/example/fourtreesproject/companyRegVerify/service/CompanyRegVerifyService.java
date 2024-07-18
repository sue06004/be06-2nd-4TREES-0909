package org.example.fourtreesproject.companyRegVerify.service;
;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import org.example.fourtreesproject.common.BaseResponse;
import org.example.fourtreesproject.companyRegVerify.dto.CompanyRegVeriifyDto;
import org.example.fourtreesproject.companyRegVerify.model.entity.CompanyRegVerify;
import org.example.fourtreesproject.companyRegVerify.model.request.CompanyRegVerifyRequest;
import org.example.fourtreesproject.companyRegVerify.model.response.CompanyRegVerifyResponse;
import org.example.fourtreesproject.companyRegVerify.repository.CompanyRegVerifyRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import org.springframework.web.client.RestTemplate;

import static org.example.fourtreesproject.common.BaseResponseStatus.*;

import java.util.*;


@Service
@RequiredArgsConstructor
public class CompanyRegVerifyService {
    @Value("${api.nts-businessman.v1.validate.serviceKey}")
    private String serviceKey;
    @Value("${api.nts-businessman.v1.validate.url}")
    private String apiUrl;

    private final CompanyRegVerifyRepository companyRegVerifyRepository;

    // 사업자동륵번호 진위확인 API
    private ResponseEntity<String> sendValidateApi(CompanyRegVerifyRequest companyRegVerifyRequest) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("businesses", Collections.singletonList(
                companyRegVerifyRequest
        ));

        Gson gson = new Gson();
        String jsonPayload = gson.toJson(payload);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
        headers.add(HttpHeaders.AUTHORIZATION, "Infuser " + serviceKey);
        HttpEntity<String> requestEntity = new HttpEntity<>(jsonPayload, headers);

        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.exchange(
                apiUrl,
                HttpMethod.POST,
                requestEntity,
                String.class
        );
    }

    // TODO : 예외처리
    public BaseResponse verify(CompanyRegVerifyRequest companyRegVerifyRequest) {

        ResponseEntity<String> response = sendValidateApi(companyRegVerifyRequest);

        // 응답이 200 ok
        if (response.getStatusCode().is2xxSuccessful()) {
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(response.getBody(), JsonObject.class);

            JsonArray dataArray = jsonObject.getAsJsonArray("data");
            JsonObject dataObject = dataArray.get(0).getAsJsonObject();
            String valid = dataObject.get("valid").getAsString();
            String b_no = dataObject.get("b_no").getAsString();

            if(valid.equals("01")) { // 일치할경우, valid: 01
                return new BaseResponse<>(
                        save(
                            CompanyRegVeriifyDto.builder()
                                    .uuid(UUID.randomUUID().toString())
                                    .companyReg(b_no)
                                    .build()
                        )
                );
            }
            else {
                // 일치하지 않을 경우, valid: 02
                return new BaseResponse<>(USER_BUSINESS_NUMBER_AUTH_FAIL);
            }
        } else {
            return new BaseResponse<>(USER_BUSINESS_NUMBER_AUTH_FAIL);
        }
    }

    public CompanyRegVerifyResponse save(CompanyRegVeriifyDto companyRegVeriifyDto) {
        CompanyRegVerify companyRegVerify = CompanyRegVerify.builder()
                .uuid(companyRegVeriifyDto.getUuid())
                .companyReg(companyRegVeriifyDto.getCompanyReg())
                .build();
        companyRegVerifyRepository.save(companyRegVerify);

        return CompanyRegVerifyResponse.builder()
                .uuid(companyRegVerify.getUuid())
                .build();
    }
}
