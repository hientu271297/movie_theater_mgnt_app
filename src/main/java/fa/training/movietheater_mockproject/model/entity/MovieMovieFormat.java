package fa.training.movietheater_mockproject.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class MovieMovieFormat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long movieMovieFormatId;

    private Double movieMovieFormatPrice;
    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @ManyToOne
    @JoinColumn(name = "movie_format_id")
    private MovieFormat movieFormat;

//    @OneToMany(mappedBy = "movieMovieFormat")
//    private Set<Ticket> tickets;

    @OneToMany(mappedBy = "movieMovieFormat")
    private Set<Schedule> scheduleSet;


}
