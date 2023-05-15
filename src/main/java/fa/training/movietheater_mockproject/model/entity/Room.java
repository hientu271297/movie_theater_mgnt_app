package fa.training.movietheater_mockproject.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fa.training.movietheater_mockproject.model.dto.MemberDto;
import fa.training.movietheater_mockproject.model.dto.RoomDetailDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;


@SqlResultSetMapping(
        name = "Room.GetRoomListMapping",
        classes = @ConstructorResult(
                targetClass = RoomDetailDto.class,
                columns = {
                        @ColumnResult(name = "room_id", type = Long.class),
                        @ColumnResult(name = "room_name", type = String.class),
                        @ColumnResult(name = "seat_quantity", type = Integer.class),
                        @ColumnResult(name = "status", type = Boolean.class),
                        @ColumnResult(name = "cinema_name", type = String.class),
                        @ColumnResult(name = "room_type_name", type = String.class)
                }
        )
)

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Room extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomId;

    @Column(nullable = false)
    private String roomName;

    // Phòng Hỏng hay ko
    @Column(nullable = false)
    private Boolean status;

    private Integer seatQuantity;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "room_type_id")
    private RoomType roomType;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "cinema_id")
    private Cinema cinema;
    @JsonIgnore
    @OneToMany(mappedBy = "room")
    private Set<Schedule> schedules;
    @JsonIgnore
    @OneToMany(mappedBy = "room")
    private Set<Seat> seats;

}
