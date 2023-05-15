package fa.training.movietheater_mockproject.controller.api;

import fa.training.movietheater_mockproject.model.dto.bookingdto.ScheduleDto;
import fa.training.movietheater_mockproject.model.entity.*;
import fa.training.movietheater_mockproject.security.SecurityUtil;
import fa.training.movietheater_mockproject.service.*;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.chrono.ChronoLocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
public class BookingScheduleControllerApi {
    MovieMovieFormatService movieMovieFormatService;
    MovieService movieService;
    MovieFormatService movieFormatService;

    ScheduleService scheduleService;

    EmployeeService employeeService;


    @PostMapping("/api/booking-schedule")
    public ResponseEntity<List<ScheduleDto>> displaySchedule(@Valid @RequestParam(value = "movieId") Long movieId,
                                                             @Valid @RequestParam("movieFormatId") Long movieFormatId) {
        Movie movie = movieService.findById(movieId).orElseThrow(() -> new RuntimeException("Not Found Movie Id"));
        MovieFormat movieFormat =movieFormatService.findByMovieFormatId(movieFormatId);
        //Get movieMovieFormat
        MovieMovieFormat movieMovieFormat = movieMovieFormatService.findMovieMovieFormatByMovieAndMovieFormat(movie, movieFormat)
                .orElseThrow(() -> new RuntimeException("Not Found MovieMovieFormat"));

        //Get scheduleList by CinemaId
        String currentEmployee = SecurityUtil.getCurrentUserLogin()
                .orElseThrow(() -> new RuntimeException("Not found current Employee Login"));
        Employee employee = employeeService.findByEmail(currentEmployee).get();
        Long employeeId = employee.getEmployeeId();
        Long movieMovieFormatId = movieMovieFormat.getMovieMovieFormatId();
        List<Schedule> scheduleList = scheduleService.findScheduleByCinemaIdAndMovieMovieFormatId(employeeId, movieMovieFormatId);
        LocalTime timeNow = LocalTime.now();
        LocalDate today = LocalDate.now();

            List<ScheduleDto> scheduleDtoList = scheduleList.stream()
                    .filter(schedule -> schedule.getDate().isAfter(today) || schedule.getDate().isEqual(today))
                    .filter(schedule ->schedule.getStartAt().isAfter(timeNow))
                    .map(schedule -> {
                        ScheduleDto scheduleDto = new ScheduleDto();
                        BeanUtils.copyProperties(schedule, scheduleDto);
                        return scheduleDto;
                    })
                    .collect(Collectors.toList());

        return ResponseEntity.ok(scheduleDtoList);
    }
}
