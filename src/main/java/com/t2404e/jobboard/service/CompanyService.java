package com.t2404e.jobboard.service;

import com.t2404e.jobboard.entity.Company;
import com.t2404e.jobboard.repository.CompanyRepository;
import com.t2404e.jobboard.repository.JobPostingRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final JobPostingRepository jobPostingRepository;

    public CompanyService(CompanyRepository companyRepository,
                          JobPostingRepository jobPostingRepository) {
        this.companyRepository = companyRepository;
        this.jobPostingRepository = jobPostingRepository;
    }

    // Dùng cho Home
    public List<Company> findAll() {
        return companyRepository.findAll();
    }

    // Dùng cho trang list + search + phân trang
    public Page<Company> findPaginatedAndFiltered(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);

        if (keyword != null && !keyword.isEmpty()) {
            return companyRepository.findByNameContainingIgnoreCase(keyword, pageable);
        }
        return companyRepository.findAll(pageable);
    }

    public Optional<Company> findById(Long id) {
        return companyRepository.findById(id);
    }

    public Company save(Company company) {
        return companyRepository.save(company);
    }

    /**
     * Xoá company:
     * 1. Xoá tất cả job thuộc company đó
     * 2. Rồi mới xoá company
     */
    @Transactional
    public void deleteById(Long id) {
        // Xoá tất cả job của company trước
        jobPostingRepository.deleteAll(
                jobPostingRepository.findByCompanyId(id)
        );

        // Sau đó xoá company
        companyRepository.deleteById(id);
    }
}
