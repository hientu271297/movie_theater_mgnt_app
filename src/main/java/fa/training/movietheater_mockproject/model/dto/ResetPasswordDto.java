package fa.training.movietheater_mockproject.model.dto;

import fa.training.movietheater_mockproject.util.PatternRegex;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Pattern;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResetPasswordDto {
    @Pattern(regexp = PatternRegex.PASSWORD,message = "Password minimum 6 characters, 1 uppercase letter, 1 lowercase letter, 1 number!")
    private String password;
    private String confirmPassword;
}
