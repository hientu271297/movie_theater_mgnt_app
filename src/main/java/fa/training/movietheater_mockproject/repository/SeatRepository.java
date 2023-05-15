package fa.training.movietheater_mockproject.repository;

import fa.training.movietheater_mockproject.model.entity.Room;
import fa.training.movietheater_mockproject.model.entity.Seat;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;


public interface SeatRepository extends PagingAndSortingRepository<Seat, Long>,
        JpaSpecificationExecutor<Seat> {

    Optional<Seat> findBySeatIdAndDeletedFalse(Long id);


    Seat findBySeatNameAndRoom(String seatName, Room room);
    List<Seat> findAllByRoomOrderBySeatName(Room room);

    @Query(value = "SELECT * FROM \n" +
            "seat s \n" +
            "JOIN room r ON s.room_id = r.room_id\n" +
            "JOIN schedule sch on sch.room_id = r.room_id\n" +
            "WHERE sch.schedule_id = ?1 AND s.deleted = 0",
            nativeQuery = true)
    List<Seat> getSeatsByScheduleId(Long scheduleId);

}
