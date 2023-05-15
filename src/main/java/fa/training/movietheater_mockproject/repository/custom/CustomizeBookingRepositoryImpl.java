package fa.training.movietheater_mockproject.repository.custom;

import fa.training.movietheater_mockproject.model.dto.BookingDto;
import org.hibernate.query.NativeQuery;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CustomizeBookingRepositoryImpl implements CustomizeBookingRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<BookingDto> getBookingDtoList(String keyword, Integer size, Integer offset) {
        String sql = "SELECT bi.bill_id, bi.member_id, me.full_name, e.full_name as employee_name , me.phone, pc.point_card_id, sch.start_at, sch.date, m.movie_name FROM movie_theater_mock_project.bill bi \n" +
                "LEFT JOIN movie_theater_mock_project.member me ON bi.member_id = me.member_id \n" +
                "LEFT JOIN movie_theater_mock_project.points_card pc ON me.member_id = pc.member_id\n" +
                "JOIN movie_theater_mock_project.employee e ON e.employee_id = bi.employee_id\n" +
                "JOIN movie_theater_mock_project.schedule sch ON sch.schedule_id = bi.schedule_id\n" +
                "JOIN movie_theater_mock_project.movie_movie_format mmf ON mmf.movie_movie_format_id = sch.movie_movie_format_id\n" +
                "JOIN movie_theater_mock_project.movie m ON m.movie_id = mmf.movie_id\n" +
                "WHERE m.movie_name LIKE :keyword OR\n" +
                "\t    me.phone LIKE :keyword OR \n" +
                "      me.full_name LIKE :keyword OR \n" +
                "      pc.point_card_id LIKE :keyword \n" +
                "ORDER BY bi.last_modified_date DESC" +
                " LIMIT :size OFFSET :page";
        Map<String, Object> params = new HashMap<>();
        params.put("keyword", "%" + keyword + "%");
        params.put("size", size);
        params.put("page", offset);

        return entityManager.createNativeQuery(sql, "Booking.GetBookingListMapping")
                .unwrap(NativeQuery.class)
                .setProperties(params)
                .getResultList();
    }

    @Override
    public Optional<Integer> totalBookingDtoListAll() {
        String sql = "SELECT bi.bill_id, bi.member_id, me.full_name, e.full_name as employee_name , me.phone, pc.point_card_id, sch.start_at, sch.date, m.movie_name FROM movie_theater_mock_project.bill bi \n" +
                "LEFT JOIN movie_theater_mock_project.member me ON bi.member_id = me.member_id \n" +
                "LEFT JOIN movie_theater_mock_project.points_card pc ON me.member_id = pc.member_id\n" +
                "JOIN movie_theater_mock_project.employee e ON e.employee_id = bi.employee_id\n" +
                "JOIN movie_theater_mock_project.schedule sch ON sch.schedule_id = bi.schedule_id\n" +
                "JOIN movie_theater_mock_project.movie_movie_format mmf ON mmf.movie_movie_format_id = sch.movie_movie_format_id\n" +
                "JOIN movie_theater_mock_project.movie m ON m.movie_id = mmf.movie_id";

        return Optional.of(entityManager.createNativeQuery(sql, "Booking.GetBookingListMapping")
                .unwrap(NativeQuery.class)
                .getResultList().size());
    }
}
