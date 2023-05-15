package fa.training.movietheater_mockproject.service.impl;

import fa.training.movietheater_mockproject.model.entity.MovieMovieFormat;
import fa.training.movietheater_mockproject.model.entity.Room;
import fa.training.movietheater_mockproject.model.entity.Schedule;
import fa.training.movietheater_mockproject.repository.ScheduleRepository;
import fa.training.movietheater_mockproject.service.ScheduleService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {
    private final ScheduleRepository scheduleRepository;



    @Override
    public List<Schedule> getScheduleWithMovieAndMovieFormat(MovieMovieFormat movieMovieFormat) {
        return scheduleRepository.getAllByDeletedFalseAndMovieMovieFormatEquals(movieMovieFormat);
    }

    @Override
    public Optional<Schedule> findScheduleById(Long scheduleId) {
        return scheduleRepository.findScheduleByScheduleIdAndDeletedFalse(scheduleId);
    }


    @Override
    public void createNew(Schedule schedule) {
        schedule.setDeleted(false);
        scheduleRepository.save(schedule);
    }

    @Override
    public List<Schedule> getSchedulesByDateAndRoom(Room room, LocalDate date) {
        return scheduleRepository.findAllByRoomAndDateAndDeletedFalse(room,date);
    }

    @Override
    public void delete(Schedule schedule) {
        schedule.setDeleted(true);
        scheduleRepository.save(schedule);
    }

    @Override
    public List<Schedule> findScheduleByCinemaIdAndMovieMovieFormatId(Long cinemaId, Long movieMovieFormatId) {
        return scheduleRepository.findScheduleByCinemaIdAndMovieMovieFormatId(cinemaId, movieMovieFormatId);
    }



    @Override
    public List<Schedule> getScheduleByRoom(Room room) {
        return scheduleRepository.getAllByRoomAndDeletedFalse(room);
    }

    @Override
    public Double getRoomTypePrice(Long scheduleId) {
        return scheduleRepository.getRoomTypePrice(scheduleId);
    }
}
