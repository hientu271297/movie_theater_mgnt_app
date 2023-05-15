package fa.training.movietheater_mockproject.model.dto;

import fa.training.movietheater_mockproject.enums.AppConstant;
import fa.training.movietheater_mockproject.model.entity.Category;
import fa.training.movietheater_mockproject.model.entity.MovieCategory;
import fa.training.movietheater_mockproject.model.entity.MovieMovieFormat;
import fa.training.movietheater_mockproject.model.entity.Schedule;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Column;
import javax.persistence.OneToMany;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
public class MovieParamDto {
    private Long movieId;

    @NotBlank(message = "{error.required}")
    private String movieName;

    private String director;

    private String actor;

    @Min(value = 0, message = "This field must be greater than 0")
    private Integer ageRestriction;

    @NotNull(message = "{error.required}")
    @Min(value = 0, message = "This field must be greater than 0")
    private Integer duration;

    @NotNull(message = "{error.required}")
    @Min(value = 0, message = "This field must be greater than 0")
    private Integer year;

    private String imageSmallUrl;

    private String imageLargeUrl;


    @DateTimeFormat(pattern = AppConstant.DATE_PATTERN)
    private LocalDate earlyDate;


    @NotNull(message = "{error.required}")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;


    @NotNull(message = "{error.required}")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;


    private String trailer;

    @NotNull(message = "{error.required}")
    @Min(value = 0, message = "This field must be greater than 0")
    private Double moviePrice;

    private String description;

    private MultipartFile imageSmall;
    private MultipartFile imageLarge;
    private Boolean hottedMovie;
}
