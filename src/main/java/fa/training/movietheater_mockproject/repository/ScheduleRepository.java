package fa.training.movietheater_mockproject.repository;

import fa.training.movietheater_mockproject.model.entity.MovieMovieFormat;
import fa.training.movietheater_mockproject.model.entity.Room;
import fa.training.movietheater_mockproject.model.entity.Schedule;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ScheduleRepository extends PagingAndSortingRepository<Schedule, Long>,
        JpaSpecificationExecutor<Schedule> {

    List<Schedule> getAllByDeletedFalseAndMovieMovieFormatEquals(MovieMovieFormat movieMovieFormat);

    Optional<Schedule> findScheduleByScheduleIdAndDeletedFalse(Long scheduleId);

    List<Schedule> getAllByRoomAndDeletedFalse(Room room);

    @Query(value = "SELECT rt.room_type_price FROM \n" +
            "schedule s\n" +
            "JOIN room r ON r.room_id = s.room_id\n" +
            "JOIN room_type rt ON rt.room_type_id = r.room_type_id\n" +
            "WHERE s.schedule_id = ?1",
            nativeQuery = true)
    Double getRoomTypePrice(Long scheduleId);
    List<Schedule> findAllByRoomAndDateAndDeletedFalse(Room room, LocalDate date);

    @Query(value = "SELECT s.* FROM  schedule s JOIN room r ON s.room_id = r.room_id \n" +
            "JOIN movie_movie_format mmf ON s.movie_movie_format_id = mmf.movie_movie_format_id\n" +
            "WHERE r.cinema_id = ?1 AND mmf.movie_movie_format_id = ?2 AND s.deleted = 0 AND r.deleted = 0 ",
            nativeQuery = true)
    List<Schedule> findScheduleByCinemaIdAndMovieMovieFormatId(Long cinemaId, Long movieMovieFormatId);


}
