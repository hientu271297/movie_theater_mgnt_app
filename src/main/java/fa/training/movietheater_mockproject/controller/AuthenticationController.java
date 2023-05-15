package fa.training.movietheater_mockproject.controller;

import fa.training.movietheater_mockproject.enums.AppConstant;
import fa.training.movietheater_mockproject.model.dto.EmailDto;
import fa.training.movietheater_mockproject.model.dto.MailerDto;
import fa.training.movietheater_mockproject.model.dto.ResetPasswordDto;
import fa.training.movietheater_mockproject.model.dto.UserLoginDto;
import fa.training.movietheater_mockproject.model.entity.Employee;
import fa.training.movietheater_mockproject.service.EmailService;
import fa.training.movietheater_mockproject.service.EmployeeService;
import lombok.AllArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.apache.commons.text.StringSubstitutor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
@AllArgsConstructor
public class AuthenticationController {
    private final EmployeeService employeeService;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String showLogin(Model model) {
        model.addAttribute("userLoginDto", new UserLoginDto());
        return "auth/login";
    }

    @GetMapping("/forget-password")
    public String showForgetPassword() {

        return "auth/forget-password";
    }

    @PostMapping("/forget-password")
    public String sendMail(@RequestParam(name = "email") String email, Model model) throws IOException {
        Optional<Employee> employee = employeeService.findByEmail(email);
        boolean errorEmail = false;
        boolean success = false;
        if (employee.isEmpty()) {
            errorEmail = true;
            model.addAttribute("errorEmail", errorEmail);
            model.addAttribute("success", success);
            return "auth/forget-password";
        }
        String resetToken = UUID.randomUUID().toString();
        Employee employeeToken = employee.get();
        employeeToken.setResetToken(resetToken);
        employeeToken.setResetTokenExpiryTime(LocalDateTime.now().plusHours(1));
        employeeService.saveEmployee(employeeToken);
        model.addAttribute("errorEmail", errorEmail);
        success = true;
        model.addAttribute("success", success);

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");
        String dateTime = LocalDateTime.now().format(dateTimeFormatter);

        //Create host mail
        String hostEmail = "nguyenvannam1999.00@gmail.com"; //your host email
        String hostName = "Cinema demo"; //your host name

        //Create "mail to" list
        MailerDto toMailer = new MailerDto(employeeToken.getEmail(), employeeToken.getFullName()); //mail to
        List<MailerDto> toMailerList = Arrays.asList(toMailer);

        //Create mail body
        String mailBody = FileUtils.readFileToString(new File("send-mail.html"), "UTF-8");
        //Pasting dynamic content to mail template
        Map<String, String> mailAttributes = new HashMap<>();
        mailAttributes.put("name", toMailer.getName());
        String linkMail = "http://localhost:8080/manage-cinema/reset-password/" + resetToken;
        mailAttributes.put("linkMail", linkMail);
        mailAttributes.put("timeMail",dateTime);
        StringSubstitutor stringSubstitutor = new StringSubstitutor(mailAttributes);
        mailBody = stringSubstitutor.replace(mailBody);

        EmailDto sendEmail = EmailDto.builder()
                .from(new MailerDto(hostEmail, hostName))
                .to(toMailerList)
                .subject("Change cinema staff password ["+dateTime+"]")
                .htmlPart(mailBody)
                .build();
        System.out.println(emailService.sendEmail(sendEmail) ? "Send success" : "Send failed");

        return "auth/forget-password";
    }

    @GetMapping("/reset-password/{resetToken}")
    public String showResetPasswordForm(@PathVariable("resetToken") String resetToken, Model model){
        Optional<Employee> employee = employeeService.findByResetToken(resetToken);
        boolean errorPassword = false;
        boolean errorToken = false;
        boolean errorTokenTime = false;
        if (employee.isEmpty()){
            errorToken = true;
        }else {
            Employee employeeToken = employee.get();
            if (employeeToken.getResetTokenExpiryTime().isBefore(LocalDateTime.now())){
                errorTokenTime = true;
            }
        }

        model.addAttribute("errorPassword",errorPassword);
        model.addAttribute("errorToken",errorToken);
        model.addAttribute("errorTokenTime",errorTokenTime);
        model.addAttribute("resetToken",resetToken);
        model.addAttribute( "resetPasswordDto",new ResetPasswordDto());
        return "auth/reset-password";
    }
    @PostMapping("/reset-password/{resetToken}")
    public String resetPasswordForm(@PathVariable("resetToken") String resetToken,
                                    @Valid ResetPasswordDto resetPasswordDto,
                                    BindingResult bindingResult, Model model){

        Optional<Employee> employee = employeeService.findByResetToken(resetToken);
        boolean errorPassword = false;
        boolean errorToken = false;
        boolean errorTokenTime = false;
        model.addAttribute("errorToken",errorToken);
        model.addAttribute("errorTokenTime",errorTokenTime);
        model.addAttribute("resetToken",resetToken);

        if (bindingResult.hasErrors()){
            model.addAttribute("errorPassword",errorPassword);
            return "auth/reset-password";
        }
        if (!resetPasswordDto.getPassword().equals(resetPasswordDto.getConfirmPassword())){
            errorPassword = true;
            model.addAttribute("errorPassword",errorPassword);
            return "auth/reset-password";
        }

        Employee employeeToken = employee.get();

        employeeToken.setPassword(passwordEncoder.encode(resetPasswordDto.getPassword()));
        employeeToken.setResetToken(null);
        employeeToken.setResetTokenExpiryTime(null);
        employeeService.saveEmployee(employeeToken);

        return "redirect:/login";
    }

}
