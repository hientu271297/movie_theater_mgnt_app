package fa.training.movietheater_mockproject.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@ToString
public class Cinema extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cinemaId;

    @Column(nullable = false)
    private String cinemaName;

    private String address;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "city_id")
    private City city;

    @OneToMany(mappedBy = "cinema",fetch = FetchType.EAGER)
    private List<Room> rooms;

    @JsonIgnore
    @OneToMany(mappedBy = "cinema")
    private List<Employee> employees;
}
