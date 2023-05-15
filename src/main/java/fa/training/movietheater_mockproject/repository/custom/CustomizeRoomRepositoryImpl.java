package fa.training.movietheater_mockproject.repository.custom;

import fa.training.movietheater_mockproject.model.dto.BookingDto;
import fa.training.movietheater_mockproject.model.dto.RoomDetailDto;
import org.hibernate.query.NativeQuery;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CustomizeRoomRepositoryImpl implements CustomizeRoomRepository{

    @PersistenceContext
    private EntityManager entityManager;
    @Override
    public List<RoomDetailDto> getRoomDetailDtoList(String keyword, Integer size, Integer offset) {
        String sql = "SELECT ro.room_id, ro.room_name, ro.seat_quantity, ro.status, ci.cinema_name, rt.room_type_name \n" +
                " FROM movie_theater_mock_project.room ro \n" +
                " JOIN movie_theater_mock_project.cinema ci ON ro.cinema_id = ci.cinema_id\n" +
                " JOIN movie_theater_mock_project.room_type rt ON ro.room_type_id = rt.room_type_id\n" +
                " WHERE ro.deleted = 0 AND (ro.room_name LIKE :keyword\n" +
                " OR ci.cinema_name LIKE :keyword\n" +
                " OR rt.room_type_name LIKE :keyword)\n" +
                " LIMIT :size OFFSET :page ";


        Map<String, Object> params = new HashMap<>();
        params.put("keyword", "%" + keyword + "%");
        params.put("size", size);
        params.put("page", offset);

        return entityManager.createNativeQuery(sql, "Room.GetRoomListMapping")
                .unwrap(NativeQuery.class)
                .setProperties(params)
                .getResultList();

    }

    @Override
    public Optional<Integer> totalRoomDetailDtoListAll() {
        String sql = "SELECT ro.room_id, ro.room_name, ro.seat_quantity, ro.status, ci.cinema_name, rt.room_type_name \n" +
                "FROM movie_theater_mock_project.room ro \n" +
                "JOIN movie_theater_mock_project.cinema ci ON ro.cinema_id = ci.cinema_id\n" +
                "JOIN movie_theater_mock_project.room_type rt ON ro.room_type_id = rt.room_type_id\n" +
                "WHERE ro.deleted = 0";


        return Optional.of(entityManager.createNativeQuery(sql, "Room.GetRoomListMapping")
                .unwrap(NativeQuery.class)
                .getResultList().size());
    }
}
