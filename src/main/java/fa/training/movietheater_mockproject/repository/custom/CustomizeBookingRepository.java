package fa.training.movietheater_mockproject.repository.custom;

import fa.training.movietheater_mockproject.model.dto.BookingDto;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


public interface CustomizeBookingRepository {

    List<BookingDto> getBookingDtoList(String keyword, Integer size,Integer offset);

    Optional<Integer> totalBookingDtoListAll();
}
