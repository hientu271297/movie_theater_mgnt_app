package fa.training.movietheater_mockproject.model.entity;

import fa.training.movietheater_mockproject.enums.Gender;
import fa.training.movietheater_mockproject.model.dto.BookingDto;
import fa.training.movietheater_mockproject.model.dto.MemberDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

@SqlResultSetMapping(
        name = "Member.GetMemberListMapping",
        classes = @ConstructorResult(
                targetClass = MemberDto.class,
                columns = {
                        @ColumnResult(name = "member_id", type = Long.class),
                        @ColumnResult(name = "full_name", type = String.class),
                        @ColumnResult(name = "address", type = String.class),
                        @ColumnResult(name = "email", type = String.class),
                        @ColumnResult(name = "date_of_birth", type = LocalDate.class),
                        @ColumnResult(name = "phone", type = String.class),
                        @ColumnResult(name = "gender", type = String.class),
                        @ColumnResult(name = "point_card_id", type = String.class),
                        @ColumnResult(name = "point", type = Integer.class),
                        @ColumnResult(name = "experience", type = Integer.class),
                        @ColumnResult(name = "ranks", type = String.class),
                }
        )
)


@Getter
@Setter
@NoArgsConstructor
@Entity
public class Member extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    private String fullName;

    private String address;

    @Column(nullable = false, unique = true)
    private String email;

    private LocalDate dateOfBirth;

    @Column(unique = true)
    private String phone;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(nullable = false)
    private String password;

    private String avatar;

    @OneToOne(mappedBy = "member")
    private PointsCard pointsCard;

    @OneToMany(mappedBy = "member")
    private Set<Bill> bills;

}
