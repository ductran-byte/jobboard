package com.t2404e.jobboard.controller;

import com.t2404e.jobboard.entity.JobPosting;
import com.t2404e.jobboard.entity.JobType;
import com.t2404e.jobboard.service.CompanyService;
import com.t2404e.jobboard.service.JobPostingService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/jobs")
public class JobPostingController {

    private final JobPostingService jobPostingService;
    private final CompanyService companyService;

    public JobPostingController(JobPostingService jobPostingService, CompanyService companyService) {
        this.jobPostingService = jobPostingService;
        this.companyService = companyService;
    }

    // 📋 Danh sách Job + Tìm kiếm + Phân trang + Sắp xếp
    @GetMapping
    public String listJobs(@RequestParam(defaultValue = "1") int page,
                           @RequestParam(defaultValue = "5") int size,
                           @RequestParam(required = false) String keyword,
                           @RequestParam(required = false, name = "sortField") String sortField,
                           @RequestParam(required = false, name = "sortDir") String sortDir,
                           Model model,
                           @ModelAttribute("message") String message) {

        // Default sort nếu chưa có
        if (sortField == null || sortField.isBlank()) {
            sortField = "postedDate";
        }
        if (sortDir == null || sortDir.isBlank()) {
            sortDir = "desc";
        }

        Page<JobPosting> jobPage = jobPostingService
                .findPaginatedAndFiltered(keyword, page, size, sortField, sortDir);

        model.addAttribute("jobs", jobPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", jobPage.getTotalPages());

        model.addAttribute("keyword", keyword);
        model.addAttribute("message", message);

        // ✅ Đưa thông tin sort ra view
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);

        return "job_list";
    }

    // ➕ Form thêm mới
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("jobPosting", new JobPosting());
        model.addAttribute("companies", companyService.findAll());
        model.addAttribute("jobTypes", JobType.values());
        return "job_form";
    }

    // 💾 Lưu Job
    @PostMapping("/save")
    public String saveJob(@Valid @ModelAttribute("jobPosting") JobPosting jobPosting,
                          BindingResult result,
                          RedirectAttributes redirectAttributes,
                          Model model) {
        if (result.hasErrors()) {
            model.addAttribute("companies", companyService.findAll());
            model.addAttribute("jobTypes", JobType.values());
            return "job_form";
        }

        boolean isUpdate = jobPosting.getId() != null;
        jobPostingService.save(jobPosting);

        redirectAttributes.addFlashAttribute("message",
                isUpdate ? "✅ Job updated successfully!" : "✅ New job added successfully!");
        return "redirect:/jobs";
    }

    // ✏️ Sửa Job
    @GetMapping("/edit/{id}")
    public String editJob(@PathVariable Long id, Model model) {
        JobPosting jobPosting = jobPostingService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid job ID: " + id));

        model.addAttribute("jobPosting", jobPosting);
        model.addAttribute("companies", companyService.findAll());
        model.addAttribute("jobTypes", JobType.values());
        return "job_form";
    }

    // ❌ Xóa
    @GetMapping("/delete/{id}")
    public String deleteJob(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        jobPostingService.deleteById(id);
        redirectAttributes.addFlashAttribute("message", "🗑️ Job deleted successfully!");
        return "redirect:/jobs";
    }

    // 🔍 Chi tiết job
    @GetMapping("/detail/{id}")
    public String jobDetail(@PathVariable Long id, Model model) {
        JobPosting job = jobPostingService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid job ID: " + id));
        model.addAttribute("job", job);
        return "job_detail";
    }
}
