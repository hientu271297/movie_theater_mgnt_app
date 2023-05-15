package fa.training.movietheater_mockproject.repository.custom;

import fa.training.movietheater_mockproject.model.dto.BookingDto;
import fa.training.movietheater_mockproject.model.dto.MemberDto;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomizeMemberRepository {
    List<MemberDto> getMemberDtoList(String keyword, Integer size, Integer offset);

    Optional<Integer> totalMemberDtoListAll();
}
