package fa.training.movietheater_mockproject.service.impl;

import fa.training.movietheater_mockproject.model.dto.RoomDetailDto;
import fa.training.movietheater_mockproject.model.entity.Cinema;
import fa.training.movietheater_mockproject.model.entity.Room;
import fa.training.movietheater_mockproject.repository.RoomRepository;
import fa.training.movietheater_mockproject.service.RoomService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RoomServiceImpl implements RoomService {
    RoomRepository roomRepository;
    @Override
    public List<Room> findByDeletedFalse() {
        return roomRepository.findByDeletedFalse();
    }

    @Override
    public void saveRoom(Room room) {
        room.setDeleted(false);
        roomRepository.save(room);
    }

    @Override
    public Optional<Room> getRoomById(Long id) {
        return roomRepository.findByRoomIdAndDeletedFalse(id);
    }

    @Override
    public void deleteRoom(Room room) {
        room.setDeleted(true);
        roomRepository.save(room);
    }
    @Override
    public Optional<Room> findRoomByRoomId(Long id) {
        return roomRepository.findRoomByRoomId(id);
    }

    @Override
    public Optional<Room> findRoomByScheduleId(Long scheduleId) {
        return roomRepository.findRoomByScheduleId(scheduleId);
    }

    @Override
    public List<Room> getAll() {
        return (List<Room>) roomRepository.findAll();
    }

    @Override
    public List<Room> findByCinema(Cinema cinema) {
        return roomRepository.findByCinemaEquals(cinema);
    }

    @Override
    public List<RoomDetailDto> getRoomDetailDtoList(String keyword, Integer size, Integer offset) {
        return roomRepository.getRoomDetailDtoList(keyword, size, offset);
    }

    @Override
    public Optional<Integer> totalRoomDetailDtoListAll() {
        return roomRepository.totalRoomDetailDtoListAll();
    }
}
