package fa.training.movietheater_mockproject.service;

import fa.training.movietheater_mockproject.model.entity.Room;
import fa.training.movietheater_mockproject.model.entity.Seat;

import java.util.List;
import java.util.Optional;

public interface SeatService extends BaseService<Seat, Long>{
    void addSeat(Seat seat);
    List<Seat> getListSeatByRoom(Room room);
    Seat getSeatBySeatNameAndRoom(String seatName, Room room);
    List<Seat> getSeatsByScheduleId(Long scheduleId);




}
