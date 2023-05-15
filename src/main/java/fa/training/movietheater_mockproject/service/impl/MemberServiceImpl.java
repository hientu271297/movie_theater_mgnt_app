package fa.training.movietheater_mockproject.service.impl;

import fa.training.movietheater_mockproject.model.entity.Member;
import fa.training.movietheater_mockproject.model.entity.PointsCard;
import fa.training.movietheater_mockproject.model.dto.MemberDto;
import fa.training.movietheater_mockproject.repository.MemberRepository;
import fa.training.movietheater_mockproject.service.MemberService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    public Optional<Member> findById(Long id) {
        return memberRepository.findByMemberIdAndDeletedFalse(id);
    }


    @Override
    public Optional<Member> findByEmail(String email) {
        return memberRepository.findByEmailAndDeletedFalse(email);
    }

    @Override
    public Optional<Member> findMemberByPointsCard(PointsCard pointsCard) {
        return memberRepository.findMemberByPointsCardAndDeletedFalse(pointsCard);
    }

    @Override
    public void create(Member member) {
        member.setDeleted(false);
        memberRepository.save(member);
    }
    @Override
    public List<MemberDto> getMemberList(String keyWord, Integer size, Integer offset) {
        return memberRepository.getMemberDtoList(keyWord, size, offset);
    }

    @Override
    public Optional<Integer> getTotalMember() {
        return memberRepository.totalMemberDtoListAll();
    }
}
