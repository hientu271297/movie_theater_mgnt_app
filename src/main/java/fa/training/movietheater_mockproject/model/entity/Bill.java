package fa.training.movietheater_mockproject.model.entity;

import fa.training.movietheater_mockproject.enums.BillStatus;
import fa.training.movietheater_mockproject.model.dto.BookingDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;

@SqlResultSetMapping(
        name = "Booking.GetBookingListMapping",
        classes = @ConstructorResult(
                targetClass = BookingDto.class,
                columns = {
                        @ColumnResult(name = "bill_id", type = Long.class),
                        @ColumnResult(name = "member_id", type = Long.class),
                        @ColumnResult(name = "employee_name", type = String.class),
                        @ColumnResult(name = "full_name", type = String.class),
                        @ColumnResult(name = "point_card_Id", type = String.class),
                        @ColumnResult(name = "phone", type = String.class),
                        @ColumnResult(name = "movie_name", type = String.class),
                        @ColumnResult(name = "date", type = LocalDate.class),
                        @ColumnResult(name = "start_at", type = LocalTime.class),
                }
        )
)

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Bill extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long billId;

    private LocalDateTime time;

    private Double totalPrice;

    private Boolean took;

    @Enumerated(EnumType.STRING)
    private BillStatus billPaymentStatus;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    @OneToMany(mappedBy = "bill", cascade = CascadeType.ALL)
    private Set<FoodOrder> foodOrder;

    @OneToMany(mappedBy = "bill", cascade = CascadeType.ALL)
    private Set<Ticket> ticket;

    @OneToMany(mappedBy = "bill")
    private Set<BillVoucher> billVouchers;

    @ManyToOne
    @JoinColumn(name = "payment_id")
    private Payment payment;
}
