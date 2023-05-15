package fa.training.movietheater_mockproject.repository;

import fa.training.movietheater_mockproject.model.entity.Cinema;
import fa.training.movietheater_mockproject.model.entity.Room;
import fa.training.movietheater_mockproject.model.entity.Schedule;
import fa.training.movietheater_mockproject.repository.custom.CustomizeRoomRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface RoomRepository extends PagingAndSortingRepository<Room, Long>, CustomizeRoomRepository {
    List<Room> findByDeletedFalse();
    Optional<Room> findByRoomIdAndDeletedFalse(Long id);
    Optional<Room> findRoomByRoomId(Long id);

    @Query(value = "select * from \n" +
            "room r \n" +
            "join schedule sch on r.room_id = sch.room_id\n" +
            "where schedule_id = ?1",
            nativeQuery = true)
    Optional<Room> findRoomByScheduleId(Long scheduleId);
    List<Room> findByCinemaEquals(Cinema cinema);

}
