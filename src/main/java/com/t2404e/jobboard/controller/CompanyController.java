package com.t2404e.jobboard.controller;

import com.t2404e.jobboard.entity.Company;
import com.t2404e.jobboard.entity.JobPosting;
import com.t2404e.jobboard.service.CompanyService;
import com.t2404e.jobboard.service.JobPostingService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/companies")
public class CompanyController {

    private final CompanyService companyService;
    private final JobPostingService jobPostingService;

    public CompanyController(CompanyService companyService,
                             JobPostingService jobPostingService) {
        this.companyService = companyService;
        this.jobPostingService = jobPostingService;
    }

    // 📋 Danh sách company + search + phân trang
    @GetMapping
    public String listCompanies(@RequestParam(defaultValue = "1") int page,
                                @RequestParam(defaultValue = "5") int size,
                                @RequestParam(required = false) String keyword,
                                @ModelAttribute("message") String message,
                                Model model) {

        Page<Company> companyPage = companyService.findPaginatedAndFiltered(keyword, page, size);

        model.addAttribute("companies", companyPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", companyPage.getTotalPages());
        model.addAttribute("keyword", keyword);
        model.addAttribute("message", message);

        return "company_list";
    }

    // ➕ Form tạo mới
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("company", new Company());
        return "company_form";
    }

    // ✏️ Form edit
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Company company = companyService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid company ID: " + id));
        model.addAttribute("company", company);
        return "company_form";
    }

    // 💾 Lưu (create + update)
    @PostMapping("/save")
    public String saveCompany(@Valid @ModelAttribute("company") Company company,
                              BindingResult result,
                              RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "company_form";
        }

        boolean isUpdate = company.getId() != null;
        companyService.save(company);

        redirectAttributes.addFlashAttribute(
                "message",
                isUpdate ? "✅ Company updated successfully!" : "✅ New company created successfully!"
        );

        return "redirect:/companies";
    }

    // 🗑️ Xoá
    @GetMapping("/delete/{id}")
    public String deleteCompany(@PathVariable Long id,
                                RedirectAttributes redirectAttributes) {
        companyService.deleteById(id);
        redirectAttributes.addFlashAttribute("message", "🗑️ Company deleted successfully!");
        return "redirect:/companies";
    }

    // 🔍 Chi tiết Company + danh sách Job của company đó
    @GetMapping("/{id}")
    public String companyDetail(@PathVariable Long id, Model model) {
        Company company = companyService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid company ID: " + id));

        List<JobPosting> jobs = jobPostingService.findByCompanyId(id);

        model.addAttribute("company", company);
        model.addAttribute("jobs", jobs);

        return "company_detail";
    }
}
