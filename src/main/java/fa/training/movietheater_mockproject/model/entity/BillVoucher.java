package fa.training.movietheater_mockproject.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class BillVoucher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderVoucherId;

    @ManyToOne
    @JoinColumn(name = "bill_id")
    private Bill bill;

    @ManyToOne
    @JoinColumn(name = "voucher_id")
    private Voucher voucher;
}
