package com.t2404e.jobboard.service;

import com.t2404e.jobboard.entity.JobPosting;
import com.t2404e.jobboard.repository.JobPostingRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class JobPostingService {
    private final JobPostingRepository jobPostingRepository;

    public JobPostingService(JobPostingRepository jobPostingRepository) {
        this.jobPostingRepository = jobPostingRepository;
    }

    public Page<JobPosting> findPaginatedAndFiltered(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        if (keyword != null && !keyword.isEmpty()) {
            return jobPostingRepository.findByTitleContainingIgnoreCase(keyword, pageable);
        }
        return jobPostingRepository.findAll(pageable);
    }

    public Optional<JobPosting> findById(Long id) {
        return jobPostingRepository.findById(id);
    }

    public void deleteById(Long id) {
        jobPostingRepository.deleteById(id);
    }

    public JobPosting save(JobPosting jobPosting) {
        return jobPostingRepository.save(jobPosting);
    }
}
