package fa.training.movietheater_mockproject.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class DateType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dateTypeId;

    private String dateName;

    private Double extraPrice;


    private LocalDate date;

    @OneToMany(mappedBy = "dateType")
    private Set<Schedule> schedules;
}
