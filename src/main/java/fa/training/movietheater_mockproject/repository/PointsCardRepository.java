package fa.training.movietheater_mockproject.repository;

import fa.training.movietheater_mockproject.model.entity.Member;
import fa.training.movietheater_mockproject.model.entity.PointsCard;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.security.core.parameters.P;

import java.util.Optional;

public interface PointsCardRepository extends PagingAndSortingRepository<PointsCard, Long>,
        JpaSpecificationExecutor<PointsCard> {
    Optional<PointsCard> findPointsCardByMember(Member member);

    Optional<PointsCard> findPointsCardByPointCardIdAndDeletedFalse(String pointCardId);
}
