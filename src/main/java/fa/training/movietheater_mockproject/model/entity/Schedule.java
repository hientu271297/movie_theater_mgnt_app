package fa.training.movietheater_mockproject.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Schedule extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long scheduleId;

//    @ManyToOne
//    @JoinColumn(name = "movie_id")
//    private Movie movie;

    private LocalTime startAt;

    private LocalTime endAt;

    private LocalDate date;

    private Boolean status;
    @JsonIgnore
    @OneToMany(mappedBy = "schedule")
    private Set<Bill> bills;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "date_type_id")
    private DateType dateType;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "movie_movie_format_id")
    private MovieMovieFormat movieMovieFormat;
}
