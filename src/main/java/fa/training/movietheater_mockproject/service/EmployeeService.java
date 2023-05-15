package fa.training.movietheater_mockproject.service;

import fa.training.movietheater_mockproject.model.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Optional;

public interface EmployeeService extends BaseService<Employee, Long>{

    Optional<Employee> findByEmail(String email);
    void saveEmployee (Employee employee);
    Optional<Employee> findByResetToken(String token);
    Optional<Employee> findById(Long id);
    void save(Employee employee);
    void delete(Employee employee);
    Page<Employee> getAll(Specification<Employee> specification, Pageable pageable);

    Optional<Employee> findByPhone(String phone);

    Specification<Employee> specSearch(String keyword, String filter);
}
