package org.example.fourtreesproject.companyRegVerify.repository;

import org.example.fourtreesproject.companyRegVerify.model.entity.CompanyRegVerify;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyRegVerifyRepository extends JpaRepository<CompanyRegVerify, Long> {
    Optional<CompanyRegVerify> findByCompanyRegAndUuid(String companyRegNum, String uuid);
}
