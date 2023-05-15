package fa.training.movietheater_mockproject.service;

import fa.training.movietheater_mockproject.model.entity.BaseEntity;
import org.springframework.data.jpa.domain.Specification;

public interface BaseService<E extends BaseEntity, ID> {
    default Specification<E> unDeleted() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("deleted"), false);
    }
}
