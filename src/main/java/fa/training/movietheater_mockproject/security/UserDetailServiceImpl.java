package fa.training.movietheater_mockproject.security;

import fa.training.movietheater_mockproject.model.entity.Employee;
import fa.training.movietheater_mockproject.service.EmployeeService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class UserDetailServiceImpl implements UserDetailsService {
    private final EmployeeService employeeService;

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {
        Optional<Employee> employeeOptional = employeeService.findByEmail(email);
        if (employeeOptional.isEmpty()) {
                throw new UsernameNotFoundException("Can not fund employee with username or email: " + email);
        }
        return createUserDetails(employeeOptional.get());
    }

    private User createUserDetails(Employee employee) {
        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(employee.getRole().toString()));
        return new User(employee.getEmail(), employee.getPassword(), authorities);
    }
}
