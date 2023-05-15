package fa.training.movietheater_mockproject.service.impl;

import fa.training.movietheater_mockproject.model.entity.Employee;
import fa.training.movietheater_mockproject.repository.EmployeeRepository;
import fa.training.movietheater_mockproject.service.EmployeeService;
import fa.training.movietheater_mockproject.util.DateUtils;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;

    @Override
    public Optional<Employee> findByEmail(String email) {
        return employeeRepository.findByEmailAndDeletedFalse(email);
    }

    @Override
    public void saveEmployee(Employee employee) {
        employee.setDeleted(false);
        employeeRepository.save(employee);
    }

    @Override
    public Optional<Employee> findByResetToken(String token) {
        return employeeRepository.findByResetTokenAndDeletedFalse(token);
    }

    @Override
    public Optional<Employee> findById(Long id) {
        return employeeRepository.findById(id);
    }

    @Override
    public void save(Employee employee) {
        employee.setDeleted(false);
        employeeRepository.save(employee);
    }

    @Override
    public void delete(Employee employee) {
        employee.setDeleted(true);
        employeeRepository.save(employee);
    }

    @Override
    public Page<Employee> getAll(Specification<Employee> specification, Pageable pageable) {
        return employeeRepository.findAll(specification,pageable);
    }

    @Override
    public Optional<Employee> findByPhone(String phone) {
        return employeeRepository.findByPhoneAndDeletedFalse(phone);
    }

    @Override
    public Specification<Employee> specSearch(String keyword, String filter) {
        Specification<Employee> specUndelete = unDeleted();
        Specification<Employee> spec = null;

        if(keyword != null && !keyword.isEmpty() && !Objects.equals(filter,"none")){
            switch (filter) {
                case "fullName":
                    spec = ((root, query, criteriaBuilder) ->
                            criteriaBuilder.like(root.get("fullName"), "%" + keyword + "%"));
                    break;
                case "dateOfBirth":
                    LocalDate date = DateUtils.stringToDate(keyword);
                    spec = ((root, query, criteriaBuilder) ->
                            criteriaBuilder.equal(root.get("dateOfBirth"), date));
                    break;
                case "phone":
                    spec = ((root, query, criteriaBuilder) ->
                            criteriaBuilder.like(root.get("phone"), "%" + keyword + "%"));
                    break;
                case "address":
                    spec = ((root, query, criteriaBuilder) ->
                            criteriaBuilder.like(root.get("address"), "%" + keyword + "%"));
                    break;
                case "email":
                    spec = ((root, query, criteriaBuilder) ->
                            criteriaBuilder.like(root.get("email"), "%" + keyword + "%"));
                    break;
            }

            if(spec != null) specUndelete = specUndelete.and(spec);

        }else if (keyword != null && !keyword.isEmpty() && Objects.equals(filter,"none")) {

            Specification<Employee> specOr = null;
            LocalDate date = DateUtils.stringToDate(keyword);

            if(date != null) {
                spec = ((root, query, criteriaBuilder) ->
                        criteriaBuilder.equal(root.get("dateOfBirth"), date));
                specOr = spec.or(specOr);
            }
            spec = ((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("fullName"), "%" + keyword + "%"));
            specOr = spec.or(specOr);

            spec = ((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("phone"),"%"+keyword+"%"));
            specOr = spec.or(specOr);

            spec = ((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("address"),"%"+keyword+"%"));
            specOr = spec.or(specOr);

            spec = ((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("email"),"%"+keyword+"%"));
            specOr = spec.or(specOr);
            specUndelete = specUndelete.and(specOr);
        }
        return specUndelete;
    }
}
