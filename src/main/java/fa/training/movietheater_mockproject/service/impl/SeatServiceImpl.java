package fa.training.movietheater_mockproject.service.impl;

import fa.training.movietheater_mockproject.model.entity.Room;
import fa.training.movietheater_mockproject.model.entity.Seat;
import fa.training.movietheater_mockproject.repository.SeatRepository;
import fa.training.movietheater_mockproject.service.SeatService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SeatServiceImpl implements SeatService {
    private final SeatRepository seatRepository;

    @Override
    public void addSeat(Seat seat) {
        seat.setDeleted(false);
        seatRepository.save(seat);
    }

    @Override
    public List<Seat> getListSeatByRoom(Room room) {
        return seatRepository.findAllByRoomOrderBySeatName(room);
    }

    @Override
    public Seat getSeatBySeatNameAndRoom(String seatName, Room room) {
        return seatRepository.findBySeatNameAndRoom(seatName,room);
    }

    @Override
    public List<Seat> getSeatsByScheduleId(Long scheduleId) {
        return seatRepository.getSeatsByScheduleId(scheduleId);
    }



}
