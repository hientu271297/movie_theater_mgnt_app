package fa.training.movietheater_mockproject.repository;

import fa.training.movietheater_mockproject.model.entity.Payment;
import fa.training.movietheater_mockproject.model.entity.Schedule;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface PaymentRepository extends PagingAndSortingRepository<Payment, Long>,
        JpaSpecificationExecutor<Payment> {
    Optional<Payment> findPaymentByPaymentNameAndDeletedFalse(String name);
}
