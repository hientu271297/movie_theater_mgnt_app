package fa.training.movietheater_mockproject.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@ToString
public class MovieFormat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long movieFormatId;

    @Column(nullable = false, unique = true)
    private String movieFormatName;

    @Column(nullable = false)
    private Double movieFormatPrice;
    @JsonIgnore
    @OneToMany(mappedBy = "movieFormat")
    private Set<MovieMovieFormat> movieMovieFormats;
}
