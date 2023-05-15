package fa.training.movietheater_mockproject.repository;

import fa.training.movietheater_mockproject.model.entity.Member;
import fa.training.movietheater_mockproject.model.entity.PointsCard;
import fa.training.movietheater_mockproject.repository.custom.CustomizeMemberRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface MemberRepository extends PagingAndSortingRepository<Member, Long>, JpaSpecificationExecutor<Member>, CustomizeMemberRepository {


    Optional<Member> findByMemberIdAndDeletedFalse(Long id);

    Optional<Member> findByEmailAndDeletedFalse(String email);

    Optional<Member> findMemberByPointsCardAndDeletedFalse(PointsCard pointsCard);

    Optional<Member> findMemberByPhoneAndDeletedFalse(String numberPhone);

}
