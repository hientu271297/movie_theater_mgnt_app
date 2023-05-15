package fa.training.movietheater_mockproject.service.impl;

import fa.training.movietheater_mockproject.model.entity.Category;
import fa.training.movietheater_mockproject.repository.CategoryRepository;
import fa.training.movietheater_mockproject.service.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    CategoryRepository categoryRepository;

    @Override
    public List<Category> getAllCategory() {
        return (List<Category>) categoryRepository.findAll();
    }

    @Override
    public Category getCategoryByName(String categoryName) {
        return categoryRepository.findByCategoryName(categoryName).get();
    }
    @Override
    public List<Category> findByMovieId(Long id) {
        return categoryRepository.findByMovieId(id);
    }
}
