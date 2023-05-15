package fa.training.movietheater_mockproject.repository;

import fa.training.movietheater_mockproject.model.entity.Movie;
import fa.training.movietheater_mockproject.model.entity.Room;
import fa.training.movietheater_mockproject.model.entity.Voucher;
import fa.training.movietheater_mockproject.repository.custom.CustomizeBookingRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoucherRepository extends PagingAndSortingRepository<Voucher, Long>, CustomizeBookingRepository,
        JpaSpecificationExecutor<Voucher> {

    Optional<Voucher> findByVoucherIdAndDeletedFalse(Long id);

    boolean existsByVoucherName(String voucherName);

    Optional<Voucher> findByVoucherName(String voucherName);

}
