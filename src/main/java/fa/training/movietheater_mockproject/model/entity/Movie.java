package fa.training.movietheater_mockproject.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@ToString
public class Movie extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long movieId;

    @Column(nullable = false)
    private String movieName;

    private String director;

    private String actor;

    private Integer ageRestriction;

    @Column(nullable = false)
    private Integer duration;

    @Column(nullable = false)
    private Integer year;

    private String imageSmallUrl;

    private String imageLargeUrl;

    @Column(nullable = false)
    private LocalDate earlyDate;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    private String trailer;

    @Column(nullable = false)
    private Double moviePrice;

    private String description;

    private Boolean hottedMovie;

    private String producer;

//    @OneToMany(mappedBy = "movie")
//    private Set<Schedule> schedules;

    @OneToMany(mappedBy = "movie")
    private Set<MovieCategory> movieCategories;

    @OneToMany(mappedBy = "movie")
    private Set<MovieMovieFormat> movieMovieFormats;
}
