package org.example.fourtreesproject.company.repository;

import org.example.fourtreesproject.company.model.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Long> {
}
