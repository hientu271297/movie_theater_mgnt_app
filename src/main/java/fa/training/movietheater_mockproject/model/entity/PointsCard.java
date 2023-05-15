package fa.training.movietheater_mockproject.model.entity;

import fa.training.movietheater_mockproject.enums.Rank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class PointsCard extends BaseEntity{
    @Id
    private String pointCardId;

    private Integer point;

    private Integer experience;

    @Enumerated(EnumType.STRING)
    private Rank ranks = Rank.BRONZE;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;
}
