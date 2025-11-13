package com.t2404e.jobboard.service;

import com.t2404e.jobboard.entity.JobPosting;
import com.t2404e.jobboard.repository.JobPostingRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.List;

@Service
public class JobPostingService {

    private final JobPostingRepository jobPostingRepository;

    public JobPostingService(JobPostingRepository jobPostingRepository) {
        this.jobPostingRepository = jobPostingRepository;
    }

    // ✅ Tìm kiếm + phân trang + sắp xếp
    public Page<JobPosting> findPaginatedAndFiltered(
            String keyword,
            int page,
            int size,
            String sortField,
            String sortDir
    ) {
        // Giá trị mặc định
        if (sortField == null || sortField.isBlank()) {
            sortField = "postedDate";   // sắp xếp theo ngày đăng
        }
        if (sortDir == null || sortDir.isBlank()) {
            sortDir = "desc";           // mới nhất lên trước
        }

        Sort.Direction direction = sortDir.equalsIgnoreCase("asc")
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;

        // Lưu ý: có thể sort nested như "company.name"
        Sort sort = Sort.by(direction, sortField);
        Pageable pageable = PageRequest.of(page - 1, size, sort);

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
    public List<JobPosting> findByCompanyId(Long companyId) {
        return jobPostingRepository.findByCompanyId(companyId);
    }
}
