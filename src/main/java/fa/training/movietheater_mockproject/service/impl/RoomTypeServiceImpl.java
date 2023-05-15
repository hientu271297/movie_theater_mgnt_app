package fa.training.movietheater_mockproject.service.impl;

import fa.training.movietheater_mockproject.model.entity.RoomType;
import fa.training.movietheater_mockproject.repository.RoomTypeRepository;
import fa.training.movietheater_mockproject.service.RoomTypeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class RoomTypeServiceImpl implements RoomTypeService {
    private final RoomTypeRepository roomTypeRepository;
    @Override
    public List<RoomType> getAllListRoomType() {
        return (List<RoomType>) roomTypeRepository.findAll();
    }

    @Override
    public RoomType getRoomTypeById(Long id) {
        return roomTypeRepository.findById(id).get();
    }
}
