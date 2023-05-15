package fa.training.movietheater_mockproject.service;

import fa.training.movietheater_mockproject.model.dto.RoomDetailDto;
import fa.training.movietheater_mockproject.model.entity.Cinema;
import fa.training.movietheater_mockproject.model.entity.Room;

import java.util.List;
import java.util.Optional;

public interface RoomService extends BaseService<Room,Long>{

    List<Room> findByDeletedFalse();
    void saveRoom(Room room);
    Optional<Room> getRoomById(Long id);
    void deleteRoom(Room room);
    Optional<Room> findRoomByRoomId(Long id);

    Optional<Room> findRoomByScheduleId(Long scheduleId);

    List<Room> getAll();
    List<Room> findByCinema(Cinema cinema);

    List<RoomDetailDto> getRoomDetailDtoList(String keyword, Integer size, Integer offset);
    Optional<Integer> totalRoomDetailDtoListAll();
}
