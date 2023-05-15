package fa.training.movietheater_mockproject.repository;

import fa.training.movietheater_mockproject.model.entity.Bill;
import fa.training.movietheater_mockproject.model.entity.FoodOrder;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodOrderRepository extends PagingAndSortingRepository<FoodOrder, Long> {
    List<FoodOrder> findAllByBill(Bill bill);
}
