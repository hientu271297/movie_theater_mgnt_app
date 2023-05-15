package fa.training.movietheater_mockproject.repository;

import fa.training.movietheater_mockproject.model.entity.Category;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends PagingAndSortingRepository<Category, Long> {
    Optional<Category> findByCategoryName(String categoryName);

    @Query(value = "SELECT c FROM Category c " +
            " JOIN c.movieCategories cm" +
            " WHERE cm.movie.movieId = ?1 ")
    List<Category> findByMovieId(Long id);
}
