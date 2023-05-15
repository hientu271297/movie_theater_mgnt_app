package fa.training.movietheater_mockproject.service;

import fa.training.movietheater_mockproject.model.entity.MovieMovieFormat;
import fa.training.movietheater_mockproject.model.entity.Room;
import fa.training.movietheater_mockproject.model.entity.Schedule;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ScheduleService extends BaseService<Schedule, Long>{

    List<Schedule> getScheduleWithMovieAndMovieFormat(MovieMovieFormat movieMovieFormat);

    Optional<Schedule> findScheduleById(Long scheduleId);

    Double getRoomTypePrice(Long scheduleId);
    void createNew(Schedule schedule);

    List<Schedule> getSchedulesByDateAndRoom(Room room, LocalDate date);

    void delete(Schedule schedule);

    List<Schedule> findScheduleByCinemaIdAndMovieMovieFormatId(Long cinemaId, Long movieMovieFormatId);

    List<Schedule> getScheduleByRoom(Room room);
}
