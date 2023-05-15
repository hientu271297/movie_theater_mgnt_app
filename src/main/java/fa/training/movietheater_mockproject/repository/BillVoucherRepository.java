package fa.training.movietheater_mockproject.repository;

import fa.training.movietheater_mockproject.model.entity.Bill;
import fa.training.movietheater_mockproject.model.entity.BillVoucher;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillVoucherRepository extends PagingAndSortingRepository<BillVoucher, Long> {
    List<BillVoucher> findAllByBill(Bill bill);
}
