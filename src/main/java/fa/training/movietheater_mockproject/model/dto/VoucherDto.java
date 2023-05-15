package fa.training.movietheater_mockproject.model.dto;

import jdk.jfr.Name;
import lombok.*;
import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDate;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VoucherDto {
    private Long voucherId;

    @NotBlank(message = "{error.required}")
    private String voucherName;

    @PositiveOrZero(message = "{error.positive}")
    private Integer discount;

    @PositiveOrZero(message = "{error.positive}")
    private Integer maxValue;

    @PositiveOrZero(message = "{error.positive}")
    private Integer minValue;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "{error.required}")
    private LocalDate startTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "{error.required}")
    private LocalDate endTime;

    @NotBlank(message = "{error.required}")
    private String type;

    private Boolean status;
}
