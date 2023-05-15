package fa.training.movietheater_mockproject.model.entity;

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
public class RoomType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomTypeId;

    @Column(nullable = false)
    private String roomTypeName;

    private Double roomTypePrice;

    private Integer numberHorizontalRowSeats;

    private Integer numberHorizontalColumnSeats;

    @OneToMany(mappedBy = "roomType")
    private Set<Room> rooms;
}
