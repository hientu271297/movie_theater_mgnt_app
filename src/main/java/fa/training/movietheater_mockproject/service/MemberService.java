package fa.training.movietheater_mockproject.service;

import fa.training.movietheater_mockproject.model.dto.BookingDto;
import fa.training.movietheater_mockproject.model.dto.MemberDto;
import fa.training.movietheater_mockproject.model.entity.Member;
import fa.training.movietheater_mockproject.model.entity.PointsCard;

import java.util.List;
import java.util.Optional;

public interface MemberService {

    Optional<Member> findById(Long id);

    Optional<Member> findByEmail(String email);

    Optional<Member> findMemberByPointsCard(PointsCard pointsCardId);

    void create(Member member);

    List<MemberDto> getMemberList(String keyWord, Integer size, Integer offset);

    Optional<Integer> getTotalMember();
}
