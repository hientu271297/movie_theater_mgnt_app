package fa.training.movietheater_mockproject.service;

import fa.training.movietheater_mockproject.model.entity.Member;
import fa.training.movietheater_mockproject.model.entity.PointsCard;

import java.util.Optional;

public interface PointsCardService {
    Optional<PointsCard> findPointsCardByMember(Member member);

    PointsCard findPointCardByPointCardId(String pointCardId);

}
