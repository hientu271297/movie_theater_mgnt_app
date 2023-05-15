package fa.training.movietheater_mockproject.repository.custom;

import fa.training.movietheater_mockproject.model.dto.BookingDto;
import fa.training.movietheater_mockproject.model.dto.RoomDetailDto;

import java.util.List;
import java.util.Optional;

public interface CustomizeRoomRepository {
    List<RoomDetailDto> getRoomDetailDtoList(String keyword, Integer size, Integer offset);

    Optional<Integer> totalRoomDetailDtoListAll();
}
