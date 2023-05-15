package fa.training.movietheater_mockproject.service;

import fa.training.movietheater_mockproject.model.entity.Category;

import java.util.List;

public interface CategoryService {
    List<Category> getAllCategory();

    Category getCategoryByName(String categoryName);

    List<Category> findByMovieId(Long id);
}
