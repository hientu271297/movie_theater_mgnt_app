package fa.training.movietheater_mockproject.repository;

import fa.training.movietheater_mockproject.model.entity.Employee;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends PagingAndSortingRepository<Employee, Long>,
        JpaSpecificationExecutor<Employee> {
    Optional<Employee> findByEmailAndDeletedFalse(String email);
    Optional<Employee> findByResetTokenAndDeletedFalse(String token);
    Optional<Employee> findByEmployeeIdAndDeletedFalse(Long id);
    Optional<Employee> findByPhoneAndDeletedFalse(String phone);
}
