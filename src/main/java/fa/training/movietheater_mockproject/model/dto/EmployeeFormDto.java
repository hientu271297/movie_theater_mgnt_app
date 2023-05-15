package fa.training.movietheater_mockproject.model.dto;

import fa.training.movietheater_mockproject.enums.Gender;
import fa.training.movietheater_mockproject.enums.Role;
import fa.training.movietheater_mockproject.model.entity.Cinema;
import fa.training.movietheater_mockproject.util.PatternRegex;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class EmployeeFormDto {
    private Long employeeId;
    @NotBlank
    private String fullName;

    private String address;

    @Pattern(regexp = PatternRegex.EMAIL,message = "{email.format}")
    private String email;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;
    @Pattern(regexp = PatternRegex.PHONE,message = "{phone.format}")
    private String phone;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Pattern(regexp = PatternRegex.PASSWORD,message = "password format invalid")
    private String password;
    @Pattern(regexp = PatternRegex.PASSWORD,message = "password format invalid")
    private String rePassword;

    private MultipartFile avatar;
    private Long cinemaId;
}
