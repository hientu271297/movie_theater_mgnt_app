package fa.training.movietheater_mockproject.controller;

import fa.training.movietheater_mockproject.enums.AppConstant;
import fa.training.movietheater_mockproject.enums.Role;
import fa.training.movietheater_mockproject.exception.AccessDenied;
import fa.training.movietheater_mockproject.exception.NotAcceptable;
import fa.training.movietheater_mockproject.exception.ResourceNotFound;
import fa.training.movietheater_mockproject.model.dto.EmployeeFormDto;
import fa.training.movietheater_mockproject.model.dto.EmployeeViewDto;
import fa.training.movietheater_mockproject.model.entity.Cinema;
import fa.training.movietheater_mockproject.model.entity.Employee;
import fa.training.movietheater_mockproject.model.entity.Movie;
import fa.training.movietheater_mockproject.security.SecurityUtil;
import fa.training.movietheater_mockproject.service.CinemaService;
import fa.training.movietheater_mockproject.service.EmployeeService;
import fa.training.movietheater_mockproject.service.FileService;
import fa.training.movietheater_mockproject.util.ConvertUtil;
import fa.training.movietheater_mockproject.util.DateUtils;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
@RequestMapping("/employee")
public class EmployeeController {
    private final EmployeeService employeeService;
    private final CinemaService cinemaService;
    private final PasswordEncoder passwordEncoder;

    private final FileService fileService;
    // All method in EmployeeController will have model with attribute "avatar"
//    @ModelAttribute
//    public void addAttributes(Model model) {
//        model.addAttribute("avatar", employeeService.findByEmail(SecurityUtil.getCurrentUserLogin()
//                                                .orElse("")).orElse(new Employee()).getAvatar());
//    }
    @GetMapping
    String showView(@RequestParam(name = "pageNumber") Optional<Integer> pageNumberOpt,
                    @RequestParam(name = "size") Optional<Integer> sizeOpt,
                    @RequestParam(name = "q") Optional<String> keywordOpt,
                    @RequestParam(name = "filter") Optional<String> filterOpt,
                    @RequestParam(name = "sort")Optional<String> sortOpt,
                    Model model) throws AccessDeniedException {

//        String currentLogin = SecurityUtil.getCurrentUserLogin().orElse("");

// <== information of employee already on the session in movie list ==>
//        Optional<Employee> employeeOptLogin = employeeService.findByEmail(currentLogin);
//        model.addAttribute("email",currentLogin);

        int pageNumber = pageNumberOpt.orElse(AppConstant.PAGE_NUMBER);
        int size = sizeOpt.orElse(AppConstant.SIZE);
        String keyword = keywordOpt.orElse(null);
        String filter = filterOpt.orElse(null);
        PageRequest page = PageRequest.of(pageNumber,size);
        Specification<Employee> spec = employeeService.specSearch(keyword,filter);
        String sort = sortOpt.orElse(null);

        if(sort != null && !sort.isEmpty() && !sort.equalsIgnoreCase("null")){
            page = page.withSort(Sort.by((sort.startsWith("-")) ? Sort.Order.desc(sort.substring(1)) : Sort.Order.asc(sort)));
            model.addAttribute("sort",sort);
        }

        Page<Employee> employeePage = employeeService.getAll(spec,page);
        List<EmployeeViewDto> employeeViewDtos = employeePage.toList().stream().map(e -> {
            EmployeeViewDto employeeViewDto = new EmployeeViewDto();
            BeanUtils.copyProperties(e,employeeViewDto);
            return employeeViewDto;
        }).collect(Collectors.toList());
        model.addAttribute("keyword",keyword);
        model.addAttribute("employeeList",employeeViewDtos);
        model.addAttribute("totalPage",employeePage.getTotalPages());
        model.addAttribute("pageNumber",pageNumber);
        model.addAttribute("size",size);
        model.addAttribute("filter",filter);
        return "employee/employee-list";
    }
    @GetMapping("/admin/add")
    public String formEmployee(Model model) throws AccessDeniedException{
        model.addAttribute("role", SecurityUtil.getCurrentUserRole().toString());
        model.addAttribute("employeeFormDto", new EmployeeFormDto());
        model.addAttribute("cinemas",cinemaService.getAll());
        return "employee/employee-form";
    }
    @PostMapping("/admin/add")
    public String addEmployee(@ModelAttribute("employeeFormDto") @Valid EmployeeFormDto employeeFormDto,
                              BindingResult bindingResult,Model model,
                              RedirectAttributes redirectAttributes) throws IOException, AccessDeniedException{
        model.addAttribute("role", SecurityUtil.getCurrentUserRole().toString());
        model.addAttribute("cinemas",cinemaService.getAll());
        if(bindingResult.hasErrors()){
            return "employee/employee-form";
        }
        if(!Objects.equals(employeeFormDto.getPassword(), employeeFormDto.getRePassword())){
            bindingResult.rejectValue("rePassword","rePassword.error");
        }
        if(employeeService.findByEmail(employeeFormDto.getEmail()).isPresent()){
            bindingResult.rejectValue("email","email.error");
        }
        if(employeeService.findByPhone(employeeFormDto.getPhone()).isPresent()){
            bindingResult.rejectValue("phone","phone.error");
        }

        if(bindingResult.hasErrors()) return "employee/employee-form";
        Cinema cinema = cinemaService.getCinemaById(employeeFormDto.getCinemaId());

        if(cinema == null){
            throw new ResourceNotFound("can not find cinema with id: "+employeeFormDto.getCinemaId());
        }

        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeFormDto,employee);
        employee.setPassword(passwordEncoder.encode(employee.getPassword()));
        employee.setCinema(cinema);
        employeeService.save(employee);

        if(employeeFormDto.getAvatar() != null &&
                StringUtils.hasText(employeeFormDto.getAvatar().getOriginalFilename())){
            String userFolder = "user" + employee.getEmployeeId(); // FIXME: Generate random value

                String urlFile = fileService.saveFile(employeeFormDto.getAvatar(),userFolder);
                employee.setAvatar(urlFile);
                employeeService.save(employee);
        }
        redirectAttributes.addFlashAttribute("result","add employee success");
        return "redirect:/employee";
    }
    @GetMapping("/update/{id}")
    public String formUpdate(@PathVariable(name = "id") Long id,
                             @RequestParam(name = "pageNumber") Optional<Integer> pageNumberOpt,
                             @RequestParam(name = "size") Optional<Integer> sizeOpt,
                             @RequestParam(name = "q") Optional<String> keywordOpt,
                             @RequestParam(name = "filter") Optional<String> filterOpt,
                             @RequestParam(name = "sort")Optional<String> sortOpt,
                             Model model) throws AccessDeniedException{

        Optional<Employee> employeeOpt = checkLoginInfo(id);
        EmployeeFormDto employeeFormDto = new EmployeeFormDto();
        BeanUtils.copyProperties(employeeOpt.get(), employeeFormDto);
        employeeFormDto.setCinemaId(employeeOpt.get().getCinema().getCinemaId());
        model.addAttribute("role",SecurityUtil.getCurrentUserRole().toString());

        int pageNumber = pageNumberOpt.orElse(AppConstant.PAGE_NUMBER);
        int size = sizeOpt.orElse(AppConstant.SIZE);
        String keyword = keywordOpt.orElse(null);
        String filter = filterOpt.orElse(null);
        String sort = sortOpt.orElse(null);
        model.addAttribute("cinemas",cinemaService.getAll());
        model.addAttribute("pageNumber",pageNumber);
        model.addAttribute("size",size);
        if(keyword != null) {
            model.addAttribute("filter", filter);
            model.addAttribute("keyword", keyword);
        }
        if(sort != null){
            model.addAttribute("sort",sort);
        }
        model.addAttribute("employeeFormDto", employeeFormDto);
        return "employee/employee-form";
    }
    @PostMapping("/update/{id}")
    public String updateEmployee(@PathVariable(name = "id") Long id,
                                 @Valid EmployeeFormDto employeeFormDto,
                                 BindingResult bindingResult,
                                 @RequestParam(name = "pageNumber") Optional<Integer> pageNumberOpt,
                                 @RequestParam(name = "size") Optional<Integer> sizeOpt,
                                 @RequestParam(name = "q") Optional<String> keywordOpt,
                                 @RequestParam(name = "filter") Optional<String> filterOpt,
                                 @RequestParam(name = "sort")Optional<String> sortOpt,
                                 RedirectAttributes redirectAttributes) throws AccessDeniedException, IOException{

        Optional<Employee> employeeUpdateOpt = checkLoginInfo(id);

        if(bindingResult.hasErrors()){
            return "employee/employee-form";
        }

        String oldPhone = employeeService.findById(id).orElse(new Employee()).getPhone();

        String currentPhone = employeeService.findByPhone(employeeFormDto.getPhone()).orElse(new Employee()).getPhone();

        if(currentPhone != null && !currentPhone.equals(oldPhone)){
            bindingResult.rejectValue("phone","phone.error");
        }

        if (bindingResult.hasErrors()){
            return "employee/employee-form";
        }
        Employee employeeUpdate = employeeUpdateOpt.get();
        BeanUtils.copyProperties(employeeFormDto,employeeUpdate,"employeeId","role","password");

        if(!SecurityUtil.getCurrentUserRole().equals(Role.ADMIN) && employeeFormDto.getCinemaId() !=null && !employeeFormDto.getCinemaId().equals(employeeUpdate.getCinema().getCinemaId())){
            throw new NotAcceptable("You can not update your cinema working");
        }
        employeeUpdate.setCinema(cinemaService.getCinemaById(employeeFormDto.getCinemaId()));
        employeeService.save(employeeUpdate);

        if(employeeFormDto.getAvatar() != null &&
                StringUtils.hasText(employeeFormDto.getAvatar().getOriginalFilename())){
            String userFolder = "user" + employeeUpdate.getEmployeeId();
            String urlFile = fileService.saveFile(employeeFormDto.getAvatar(),userFolder);
            employeeUpdate.setAvatar(urlFile);
            employeeService.save(employeeUpdate);
        }

        redirectAttributes.addFlashAttribute("result","update employee success");
        return "redirect:/employee";
    }

    @GetMapping("/admin/delete/{id}")
    public String deleteEmployee(@PathVariable(name = "id") Long id,
                                 @RequestParam(name = "pageNumber") Optional<Integer> pageNumberOpt,
                                 @RequestParam(name = "size") Optional<Integer> sizeOpt,
                                 @RequestParam(name = "q") Optional<String> keywordOpt,
                                 @RequestParam(name = "filter") Optional<String> filterOpt,
                                 @RequestParam(name = "sort")Optional<String> sortOpt,
                                 RedirectAttributes redirectAttributes) throws AccessDeniedException{

        Optional<Employee> employeeOpt = checkLoginInfo(id);
        String userLogin = SecurityUtil.getCurrentUserLogin().orElse(null);
        if(userLogin != null){
            Employee employeeLogin = employeeService.findByEmail(userLogin).orElse(new Employee());
            if(employeeLogin.getEmployeeId().equals(employeeOpt.get().getEmployeeId())){
                redirectAttributes.addFlashAttribute("result", "can not delete yourself");
                return "redirect:/employee";
            }
        }
        employeeService.delete(employeeOpt.get());
        redirectAttributes.addFlashAttribute("result", "delete success");
        return "redirect:/employee";
    }

    public Optional<Employee> checkLoginInfo(Long id) throws AccessDeniedException {
        Optional<String> currentLoginOpt = SecurityUtil.getCurrentUserLogin();
        Role role = SecurityUtil.getCurrentUserRole();
        String currentLogin = currentLoginOpt.orElse(null);
        Employee employeeLogin = null;
        if(currentLogin != null){
            employeeLogin = employeeService.findByEmail(currentLogin).orElse(new Employee());
        }

        Optional<Employee> employeeOpt = employeeService.findById(id);
        if(employeeOpt.isEmpty()) throw new ResourceNotFound("can not find id: "+id);

        if(employeeLogin != null
                && !Objects.equals(employeeLogin.getEmployeeId(), employeeOpt.get().getEmployeeId())
                && !Objects.equals(employeeLogin.getRole(),Role.ADMIN)){
            throw new AccessDenied("you can not edit another account");
        }
        return employeeOpt;
    }
}
