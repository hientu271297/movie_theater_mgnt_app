package fa.training.movietheater_mockproject.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Payment extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    @Column(name = "payment_name")
    private String paymentName;

    @OneToMany(mappedBy = "payment")
    private Set<Bill> bills;
}
