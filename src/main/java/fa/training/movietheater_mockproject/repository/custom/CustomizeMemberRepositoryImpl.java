package fa.training.movietheater_mockproject.repository.custom;

import fa.training.movietheater_mockproject.model.dto.BookingDto;
import fa.training.movietheater_mockproject.model.dto.MemberDto;
import org.hibernate.query.NativeQuery;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CustomizeMemberRepositoryImpl implements CustomizeMemberRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<MemberDto> getMemberDtoList(String keyword, Integer size, Integer offset) {
        String sql = "SELECT me.member_id, me.full_name, me.address, me.email, me.date_of_birth, me.phone, me.gender, pc.point_card_id, pc.point, pc.experience, pc.ranks \n" +
                "FROM movie_theater_mock_project.member me\n" +
                "JOIN movie_theater_mock_project.points_card pc \n" +
                "ON me.member_id = pc.member_id \n" +
                "WHERE me.deleted = 0 \n" +
                "AND (me.phone LIKE :keyword \n" +
                "OR me.full_name LIKE :keyword\n" +
                "OR me.email LIKE :keyword\n" +
                "OR pc.point_card_id LIKE :keyword)\n" +
                "ORDER BY pc.experience DESC\n" +
                " LIMIT :size OFFSET :page";


        Map<String, Object> params = new HashMap<>();
        params.put("keyword", "%" + keyword + "%");
        params.put("size", size);
        params.put("page", offset);

        return entityManager.createNativeQuery(sql, "Member.GetMemberListMapping")
                .unwrap(NativeQuery.class)
                .setProperties(params)
                .getResultList();
    }


    @Override
    public Optional<Integer> totalMemberDtoListAll() {
        String sql = "SELECT me.member_id, me.full_name, me.address, me.email, me.date_of_birth, me.phone, me.gender, pc.point_card_id, pc.point, pc.experience, pc.ranks \n" +
                "FROM movie_theater_mock_project.member me\n" +
                "JOIN movie_theater_mock_project.points_card pc \n" +
                "ON me.member_id = pc.member_id \n" +
                "WHERE me.deleted = 0 ";

        return Optional.of(entityManager.createNativeQuery(sql, "Member.GetMemberListMapping")
                .unwrap(NativeQuery.class)
                .getResultList().size());
    }
}
