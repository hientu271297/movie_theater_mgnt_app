package fa.training.movietheater_mockproject.model.entity;


import fa.training.movietheater_mockproject.enums.Gender;
import fa.training.movietheater_mockproject.enums.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Employee extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long employeeId;

    @Column(nullable = false)
    private String fullName;

    private String address;

    @Column(nullable = false, unique = true)
    private String email;

    private LocalDate dateOfBirth;

    @Column(nullable = false, unique = true)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(nullable = false)
    private String password;

    private String avatar;

    @Column(name = "reset_token")
    private String resetToken;

    @Column(name = "reset_token_expiry_time")
    private LocalDateTime resetTokenExpiryTime;

    @OneToMany(mappedBy = "employee")
    private Set<Bill> bills;

    @ManyToOne
    @JoinColumn(name = "cinema_id")
    private Cinema cinema;


}
