package fa.training.movietheater_mockproject.security;

import fa.training.movietheater_mockproject.model.entity.Employee;
import fa.training.movietheater_mockproject.service.EmployeeService;
import lombok.AllArgsConstructor;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Component
@AllArgsConstructor
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final EmployeeService employeeService;
    @Override
    protected String determineTargetUrl(HttpServletRequest request,
                                        HttpServletResponse response) {
        if(request.getSession().getAttribute("id") == null){
            String currentLogin = SecurityUtil.getCurrentUserLogin().orElse(null);
            Optional<Employee> employeeOptLogin = employeeService.findByEmail(currentLogin);
            request.getSession().setAttribute("avatar",employeeOptLogin.get().getAvatar());
            request.getSession().setAttribute("name",employeeOptLogin.get().getFullName());
            request.getSession().setAttribute("email",employeeOptLogin.get().getEmail());
            request.getSession().setAttribute("id",employeeOptLogin.get().getEmployeeId());
            if(employeeOptLogin.get().getCinema() != null){
                request.getSession().setAttribute("cinemaName",employeeOptLogin.get().getCinema().getCinemaName());
                request.getSession().setAttribute("cinemaId",employeeOptLogin.get().getCinema().getCinemaId());
            }
        }
        return "/movie";
    }
}
