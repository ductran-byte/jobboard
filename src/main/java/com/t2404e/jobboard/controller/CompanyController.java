package com.t2404e.jobboard.controller;

import com.t2404e.jobboard.entity.Company;
import com.t2404e.jobboard.service.CompanyService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/companies")
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    // 📋 Hiển thị danh sách công ty
    @GetMapping
    public String listCompanies(Model model, @ModelAttribute("message") String message) {
        model.addAttribute("companies", companyService.findAll());
        if (message != null && !message.isEmpty()) {
            model.addAttribute("message", message);
        }
        return "company_list";
    }

    // ➕ Form thêm công ty
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("company", new Company());
        return "company_form";
    }

    // 💾 Lưu (thêm/sửa)
    @PostMapping("/save")
    public String saveCompany(@Valid @ModelAttribute("company") Company company,
                              BindingResult result,
                              RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "company_form";
        }

        boolean isUpdate = company.getId() != null;
        companyService.save(company);

        if (isUpdate) {
            System.out.println("🔄 Updated company: " + company.getName());
            redirectAttributes.addFlashAttribute("message",
                    "✅ Company updated successfully!");
        } else {
            System.out.println("➕ Added new company: " + company.getName());
            redirectAttributes.addFlashAttribute("message",
                    "✅ New company added successfully!");
        }

        return "redirect:/companies";
    }

    // ✏️ Form sửa
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Company company = companyService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid company ID: " + id));
        model.addAttribute("company", company);
        return "company_form";
    }

    // ❌ Xóa công ty
    @GetMapping("/delete/{id}")
    public String deleteCompany(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        companyService.deleteById(id);
        System.out.println("🗑️ Deleted company ID: " + id);
        redirectAttributes.addFlashAttribute("message", "🗑️ Company deleted successfully!");
        return "redirect:/companies";
    }
}
