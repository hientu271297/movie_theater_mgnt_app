package fa.training.movietheater_mockproject.model.dto;

import fa.training.movietheater_mockproject.model.entity.Category;
import fa.training.movietheater_mockproject.model.entity.MovieFormat;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class MovieWithFormatDto {
    private Long movieId;

    private String movieName;

    private String director;

    private String actor;

    private Integer ageRestriction;


    private Integer duration;


    private Integer year;

    private String imageSmallUrl;

    private String imageLargeUrl;


    private LocalDate earlyDate;


    private LocalDate startDate;


    private LocalDate endDate;

    private String trailer;


    private Double moviePrice;

    private String description;

    private Boolean hottedMovie;

    private String producer;
    private List<MovieFormat> movieFormats;
    private List<Category> categories;
}
