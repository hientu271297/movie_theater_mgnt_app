package fa.training.movietheater_mockproject.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Voucher extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long voucherId;


    @Column(nullable = false, unique = true)
    private String voucherName;

    private Integer discount;

    private Integer maxValue;

    private Integer minValue;


    @Column(nullable = false)
    private LocalDate startTime;


    @Column(nullable = false)
    private LocalDate endTime;

    @Column(nullable = false)
    private String type;

    private Boolean status;

    @OneToMany(mappedBy = "voucher")
    Set<BillVoucher> billVouchers;

}
