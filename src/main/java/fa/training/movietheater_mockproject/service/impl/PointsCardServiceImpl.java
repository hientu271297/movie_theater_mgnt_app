package fa.training.movietheater_mockproject.service.impl;

import fa.training.movietheater_mockproject.model.entity.Member;
import fa.training.movietheater_mockproject.model.entity.PointsCard;
import fa.training.movietheater_mockproject.repository.PointsCardRepository;
import fa.training.movietheater_mockproject.service.PointsCardService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@AllArgsConstructor
public class PointsCardServiceImpl implements PointsCardService {
    private final PointsCardRepository pointsCardRepository;


    @Override
    public Optional<PointsCard> findPointsCardByMember(Member member) {
        return pointsCardRepository.findPointsCardByMember(member);
    }

    @Override
    public PointsCard findPointCardByPointCardId(String pointCardId) {
        return pointsCardRepository.findPointsCardByPointCardIdAndDeletedFalse(pointCardId).get();
    }
}
