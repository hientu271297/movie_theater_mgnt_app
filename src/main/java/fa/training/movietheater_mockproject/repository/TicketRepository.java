package fa.training.movietheater_mockproject.repository;

import fa.training.movietheater_mockproject.model.entity.Bill;
import fa.training.movietheater_mockproject.model.entity.Schedule;
import fa.training.movietheater_mockproject.model.entity.Seat;
import fa.training.movietheater_mockproject.model.entity.Ticket;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends PagingAndSortingRepository<Ticket, Long> {
    List<Ticket> findAllByBillAndDeletedFalse(Bill bill);

    @Query(value = "SELECT t.seat_id FROM Schedule s " +
            "JOIN Bill b ON s.schedule_id = b.schedule_id " +
            "JOIN TICKET t ON b.bill_id = t.bill_id\n" +
            "WHERE s.schedule_id = ?1 AND t.seat_id = ?2 ", nativeQuery = true)
    Optional<Long> findTicketByScheduleAndSeat(Long scheduleId, Long seatId);


}
