package fa.training.movietheater_mockproject.model.dto;

import fa.training.movietheater_mockproject.enums.Gender;
import fa.training.movietheater_mockproject.model.entity.Bill;
import fa.training.movietheater_mockproject.model.entity.PointsCard;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberDto {
    private Long memberId;

    private String fullName;

    private String address;

    private String email;

    private LocalDate dateOfBirth;

    private String phone;

    private String gender;


    private String pointCardId;

    private Integer point;

    private Integer experience;
    private String ranks;
}
