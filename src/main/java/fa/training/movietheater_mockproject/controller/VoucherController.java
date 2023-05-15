package fa.training.movietheater_mockproject.controller;

import fa.training.movietheater_mockproject.enums.AppConstant;
import fa.training.movietheater_mockproject.model.dto.RoomDto;
import fa.training.movietheater_mockproject.model.dto.VoucherDto;
import fa.training.movietheater_mockproject.model.entity.*;
import fa.training.movietheater_mockproject.security.SecurityUtil;
import fa.training.movietheater_mockproject.service.EmployeeService;
import fa.training.movietheater_mockproject.service.VoucherService;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
@RequestMapping("/voucher")
public class VoucherController {
    private final EmployeeService employeeService;
    private final VoucherService voucherService;
    @GetMapping
    public String listVoucher(@RequestParam(name = "pageNumber") Optional<Integer> pageNumberOpt,
                            @RequestParam(name = "size") Optional<Integer> sizeOpt,
                            @RequestParam(name = "q") Optional<String> keywordOpt,
                            @RequestParam(name = "filter") Optional<String> filterOpt,
                            @RequestParam(name = "sort")Optional<List<String>> sortsOpt,
                            Model model){

        String currentLogin = SecurityUtil.getCurrentUserLogin().orElse(null);
        Optional<Employee> employeeOptLogin = employeeService.findByEmail(currentLogin);

        int pageNumber = pageNumberOpt.orElse(AppConstant.PAGE_NUMBER);
        int size = sizeOpt.orElse(AppConstant.SIZE);
        String keyword = keywordOpt.orElse(null);
        String filter = filterOpt.orElse(null);

        Specification<Voucher> specUndelete = voucherService.unDeleted();

        List<String> sorts = sortsOpt.orElseGet(() -> Arrays.asList());


        List<Sort.Order> orders = sorts.stream()
                .filter(s -> !s.isEmpty() && !s.equalsIgnoreCase("null"))
                .map(s -> s.startsWith("-") ? Sort.Order.desc(s.substring(1)) : Sort.Order.asc(s))
                .collect(Collectors.toList());

        model.addAttribute("sorts", sorts);

        if(keyword != null && !keyword.isEmpty() && !keyword.equalsIgnoreCase("null") ){
            Specification<Voucher> spec = ((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get(filter),"%"+keyword+"%"));

            specUndelete = specUndelete.and(spec);
            model.addAttribute("keyword",keyword);
        }

        PageRequest page = PageRequest.of(pageNumber,size).withSort(Sort.by(orders));
        Page<Voucher> vouchersPage = voucherService.getAll(specUndelete,page);

        model.addAttribute("voucherList",vouchersPage.toList());
        model.addAttribute("totalPage",vouchersPage.getTotalPages());
        model.addAttribute("pageNumber",pageNumber);
        model.addAttribute("size",size);
        model.addAttribute("filter",filter);
        model.addAttribute("avatar",employeeOptLogin.get().getAvatar());
        return "/voucher/list-voucher";
    }

    @GetMapping("/delete/{id}")
    public String deleteVoucher(@PathVariable(name = "id") Long id, RedirectAttributes redirectAttributes) {
        Optional<Voucher> optionalVoucher = voucherService.getVoucherById(id);
        if (optionalVoucher.isEmpty()) {
            throw new IllegalArgumentException("Can not found entity with id: " + id);
        }
        voucherService.deleteVoucher(optionalVoucher.get());
        redirectAttributes.addFlashAttribute("result", "Delete Voucher Success!");
        return "redirect:/voucher";
    }

    @GetMapping("/add")
    public String showVoucherForm(Model model) {
        model.addAttribute("voucherDto", new VoucherDto());
        return "/voucher/voucher-form";
    }

    @PostMapping("/save")
    public String saveVoucher(@Valid VoucherDto voucherDto,BindingResult bindingResult,
                           Model model, RedirectAttributes redirectAttributes) {

        if (voucherDto.getMaxValue() != null && voucherDto.getMinValue() != null){
            if (voucherDto.getMaxValue() <= voucherDto.getMinValue()){
                bindingResult.rejectValue("maxValue","error.maxvalue");
                bindingResult.rejectValue("minValue","error.minvalue");
            }
        }
        if ( voucherDto.getStartTime() != null && voucherDto.getEndTime() != null){
            if ( voucherDto.getEndTime().isBefore(voucherDto.getStartTime())) {
                bindingResult.rejectValue("endTime", "error.endDate.invalid");
                bindingResult.rejectValue("startTime", "error.startDate.invalid");
            }
        }

    if (voucherDto.getVoucherId() == null) {
        Optional<Voucher> voucherDtoNameToCheckDuplicate = voucherService.getVoucherByName(voucherDto.getVoucherName());
         if (voucherDtoNameToCheckDuplicate.isPresent()){
        bindingResult.rejectValue("voucherName", "error.duplicate");
    }
}


        if (bindingResult.hasErrors()) {
            return "voucher/voucher-form";
        }
        Voucher voucher = new Voucher();
        BeanUtils.copyProperties(voucherDto, voucher);
        voucherService.saveVoucher(voucher);

        if (voucherDto.getVoucherId() != null) {
            redirectAttributes.addFlashAttribute("result", "Update Voucher Success!");
        } else {
            redirectAttributes.addFlashAttribute("result", "Add Voucher Success!");

        }
        return "redirect:/voucher";
    }

    @GetMapping("/update/{id}")
    public String updateVoucher(@PathVariable(name = "id") Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Voucher> optionalVoucher = voucherService.getVoucherById(id);
        if (optionalVoucher.isEmpty()) {
            throw new IllegalArgumentException("Can not found entity with id: " + id);
        }
        VoucherDto voucherDto = new VoucherDto();
        BeanUtils.copyProperties(optionalVoucher.get(), voucherDto);
        model.addAttribute("voucherDto", voucherDto);
        return "/voucher/voucher-form";
    }
}
