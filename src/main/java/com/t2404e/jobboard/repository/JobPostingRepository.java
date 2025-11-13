package com.t2404e.jobboard.repository;

import com.t2404e.jobboard.entity.JobPosting;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface JobPostingRepository extends JpaRepository<JobPosting, Long> {
    Page<JobPosting> findByTitleContainingIgnoreCase(String title, Pageable pageable);
    List<JobPosting> findByCompanyId(Long companyId);


}
