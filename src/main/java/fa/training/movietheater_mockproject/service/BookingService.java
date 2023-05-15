package fa.training.movietheater_mockproject.service;

import fa.training.movietheater_mockproject.model.dto.BookingDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public interface BookingService {
    List<BookingDto> getBookingList(String keyWord, Integer size, Integer offset);

    Optional<Integer> totalBooking();

}
