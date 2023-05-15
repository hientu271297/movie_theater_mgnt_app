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
public class Food extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long foodId;

    @Column(nullable = false, unique = true)
    private String foodName;

    @Column(nullable = false)
    private Double foodPrice;

    private Integer foodStorage;

    @Column(name = "food_img_url",length = 1000)
    private String foodImgURL;

    @OneToMany(mappedBy = "food")
    private Set<FoodOrder> foodOrders;
}
