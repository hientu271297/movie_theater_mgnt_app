package fa.training.movietheater_mockproject.repository;

import fa.training.movietheater_mockproject.model.entity.Bill;
import fa.training.movietheater_mockproject.model.entity.Schedule;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BillRepository extends PagingAndSortingRepository<Bill, Long>,
        JpaSpecificationExecutor<Bill> {
    List<Bill> findByScheduleEqualsAndDeletedFalse(Schedule schedule);

    List<Bill> getAllByDeletedFalse();

    List<Bill> getBillsBySchedule(Schedule schedule);
    Optional<Bill> findFirstByScheduleEqualsAndDeletedFalse(Schedule schedule);

}
