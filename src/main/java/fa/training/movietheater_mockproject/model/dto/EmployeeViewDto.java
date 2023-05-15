package fa.training.movietheater_mockproject.model.dto;

import fa.training.movietheater_mockproject.enums.Gender;
import fa.training.movietheater_mockproject.enums.Role;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class EmployeeViewDto {
    private Long employeeId;

    private String fullName;

    private String address;


    private String email;

    private LocalDate dateOfBirth;

    @Column(nullable = false, unique = true)
    private String phone;

    @Enumerated(EnumType.STRING)

    private Gender gender;

    @Enumerated(EnumType.STRING)
    private Role role;

    private LocalDateTime createdDate;


    private String avatar;
}
