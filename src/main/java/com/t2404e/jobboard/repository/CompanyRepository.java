package com.t2404e.jobboard.repository;

import com.t2404e.jobboard.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Long> {
}
