package fa.training.movietheater_mockproject.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class City {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cityId;

    private String cityName;

    @OneToMany(mappedBy = "city",fetch = FetchType.EAGER)
    private List<Cinema> cinemas;

}
