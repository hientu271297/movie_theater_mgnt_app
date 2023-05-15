package fa.training.movietheater_mockproject.service;

import fa.training.movietheater_mockproject.model.entity.RoomType;

import java.util.List;

public interface RoomTypeService {
    List<RoomType> getAllListRoomType();
    RoomType getRoomTypeById(Long id);
}
