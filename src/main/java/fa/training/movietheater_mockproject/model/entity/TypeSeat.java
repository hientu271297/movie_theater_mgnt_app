package fa.training.movietheater_mockproject.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class TypeSeat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long typeSeatId;

    @Column(nullable = false)
    private String typeName;

    @Column(nullable = false)
    private Double typeSeatPrice;

    @OneToMany(mappedBy = "typeSeat")
    private Set<Seat> seats;
}
