package com.t2404e.jobboard.controller;

import com.t2404e.jobboard.entity.Company;
import com.t2404e.jobboard.entity.JobPosting;
import com.t2404e.jobboard.repository.CompanyRepository;
import com.t2404e.jobboard.repository.JobPostingRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.Comparator;
import java.util.List;

@Controller
public class HomeController {
    private final CompanyRepository companyRepository;
    private final JobPostingRepository jobPostingRepository;

    public HomeController(CompanyRepository companyRepository, JobPostingRepository jobPostingRepository) {
        this.companyRepository = companyRepository;
        this.jobPostingRepository = jobPostingRepository;
    }

    @GetMapping({"/", "/home"})
    public String home(Model model) {
        List<JobPosting> latestJobs = jobPostingRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(JobPosting::getPostedDate).reversed())
                .limit(5)
                .toList();

        List<Company> topCompanies = companyRepository.findAll()
                .stream()
                .limit(5)
                .toList();

        model.addAttribute("latestJobs", latestJobs);
        model.addAttribute("topCompanies", topCompanies);
        return "home";
    }
}
