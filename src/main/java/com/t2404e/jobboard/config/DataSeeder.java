package com.t2404e.jobboard.config;

import com.t2404e.jobboard.entity.Company;
import com.t2404e.jobboard.entity.JobPosting;
import com.t2404e.jobboard.entity.JobType;
import com.t2404e.jobboard.repository.CompanyRepository;
import com.t2404e.jobboard.repository.JobPostingRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class DataSeeder implements CommandLineRunner {

    private final CompanyRepository companyRepository;
    private final JobPostingRepository jobPostingRepository;
    private final Random random = new Random();

    public DataSeeder(CompanyRepository companyRepository, JobPostingRepository jobPostingRepository) {
        this.companyRepository = companyRepository;
        this.jobPostingRepository = jobPostingRepository;
    }

    @Override
    public void run(String... args) {

        if (companyRepository.count() == 0 && jobPostingRepository.count() == 0) {

            // 🏢 10 công ty mẫu
            List<Company> companies = new ArrayList<>();
            companies.add(createCompany("Aptech Software Solutions", "123 Nguyen Trai, Hanoi", "contact@aptech.com", "https://aptechvietnam.com"));
            companies.add(createCompany("VietTech Global", "25 Hoang Quoc Viet, Hanoi", "info@viettech.vn", "https://viettech.vn"));
            companies.add(createCompany("Nexus IT", "15 Tran Hung Dao, Da Nang", "hr@nexus.vn", "https://nexusit.vn"));
            companies.add(createCompany("SoftVision Asia", "289 Le Loi, HCM", "jobs@softvision.com", "https://softvision.asia"));
            companies.add(createCompany("HanaTech Co., Ltd", "89 Nguyen Van Linh, HCM", "hello@hanatech.vn", "https://hanatech.vn"));
            companies.add(createCompany("Cloudy Systems", "92 Duy Tan, Hanoi", "team@cloudy.io", "https://cloudy.io"));
            companies.add(createCompany("NovaWorks", "55 Le Duan, Da Nang", "contact@novaworks.com", "https://novaworks.com"));
            companies.add(createCompany("NextGen Innovations", "21 Tran Phu, HCM", "career@nextgen.vn", "https://nextgen.vn"));
            companies.add(createCompany("DigitalCraft Studio", "18 Quang Trung, Hanoi", "dcstudio@gmail.com", "https://digitalcraft.vn"));
            companies.add(createCompany("GreenHub Tech", "11 Phan Dinh Phung, Da Nang", "greenhub@outlook.com", "https://greenhub.vn"));

            companyRepository.saveAll(companies);

            // 💼 Danh sách tiêu đề và mô tả ngẫu nhiên
            String[] jobTitles = {
                    "Java Backend Developer",
                    "Frontend React Developer",
                    "UI/UX Designer",
                    "Mobile App Developer",
                    "Data Analyst",
                    "DevOps Engineer",
                    "Project Manager",
                    "QA/QC Tester",
                    "AI Engineer",
                    "Business Analyst",
                    "Python Developer",
                    "Game Developer",
                    "Content Writer",
                    "Digital Marketing Specialist",
                    "Customer Support",
                    "HR Executive",
                    "Flutter Developer",
                    "NodeJS Engineer",
                    "Cloud Engineer",
                    "Intern Software Engineer"
            };

            String[] locations = {"Hanoi", "HCM City", "Da Nang", "Hai Phong", "Can Tho"};
            String[] salaries = {
                    "$800 - $1200", "$1000 - $1500", "$1200 - $2000",
                    "$1500 - $2500", "$500 - $700", "$300 - $500 Internship"
            };
            JobType[] jobTypes = JobType.values();

            // 🧠 Sinh ngẫu nhiên 20 job
            for (int i = 0; i < 20; i++) {
                Company randomCompany = companies.get(random.nextInt(companies.size()));
                JobPosting job = new JobPosting();
                job.setTitle(jobTitles[i % jobTitles.length]);
                job.setDescription("""
                        We are looking for a talented individual to join our dynamic team.
                        Responsibilities include developing and maintaining software systems,
                        collaborating across departments, and ensuring product quality.
                        """);
                job.setLocation(locations[random.nextInt(locations.length)]);
                job.setSalary(salaries[random.nextInt(salaries.length)]);
                job.setJobType(jobTypes[random.nextInt(jobTypes.length)]);
                job.setPostedDate(LocalDate.now().minusDays(random.nextInt(10)));
                job.setCompany(randomCompany);

                jobPostingRepository.save(job);
            }

            System.out.println("✅ Seeded 10 companies and 20 job postings successfully!");
        }
    }

    // 🧩 Helper method để tạo Company
    private Company createCompany(String name, String address, String email, String website) {
        Company c = new Company();
        c.setName(name);
        c.setAddress(address);
        c.setEmail(email);
        c.setWebsite(website);
        return c;
    }
}
