package fa.training.movietheater_mockproject.service.impl;

import fa.training.movietheater_mockproject.model.entity.TypeSeat;
import fa.training.movietheater_mockproject.repository.TypeSeatRepository;
import fa.training.movietheater_mockproject.service.TypeSeatService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TypeSeatServiceImpl implements TypeSeatService {
    private final TypeSeatRepository typeSeatRepository;

    @Override
    public TypeSeat getTypeSeatById(Long id) {
        return typeSeatRepository.findById(id).get();
    }
}
