package fa.training.movietheater_mockproject.service.impl;

import fa.training.movietheater_mockproject.model.dto.BookingDto;
import fa.training.movietheater_mockproject.repository.MovieRepository;
import fa.training.movietheater_mockproject.service.BookingService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final MovieRepository movieRepository;
    @Override
    public List<BookingDto> getBookingList(String keyWord, Integer size, Integer offset) {
        return movieRepository.getBookingDtoList(keyWord,size, offset);
    }

    @Override
    public Optional<Integer> totalBooking() {
        return movieRepository.totalBookingDtoListAll();
    }
}
