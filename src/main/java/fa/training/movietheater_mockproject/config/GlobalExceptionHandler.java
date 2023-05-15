package fa.training.movietheater_mockproject.config;


import fa.training.movietheater_mockproject.exception.AccessDenied;
import fa.training.movietheater_mockproject.exception.NotAcceptable;
import fa.training.movietheater_mockproject.exception.ResourceNotFound;
import fa.training.movietheater_mockproject.repository.EmployeeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import java.net.BindException;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    private final EmployeeRepository employeeRepository;

    public GlobalExceptionHandler(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @ExceptionHandler(ResourceNotFound.class)
    public ModelAndView handleResourceNotFound(ResourceNotFound r, Model model){
        model.addAttribute("message", r.getMessage());
        model.addAttribute("title","Not Acceptable");
        model.addAttribute("code","???");
        return new ModelAndView("error/error-customize");
    }
    @ExceptionHandler(AccessDenied.class)
    public ModelAndView handleResourceNotFound(AccessDenied r, Model model){
        model.addAttribute("message", r.getMessage());
        model.addAttribute("title","Access Denied");
        model.addAttribute("code","???");
        return new ModelAndView("error/error-customize");
    }

    @ExceptionHandler(NotAcceptable.class)
    public ModelAndView notAcceptableHandler(NotAcceptable e, Model model){
        model.addAttribute("message", e.getMessage());
        model.addAttribute("title","Not Acceptable");
        model.addAttribute("code","406");
        return new ModelAndView("error/error-customize");
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleBookingDuplicateSeat(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler({BindException.class, MethodArgumentNotValidException.class})
    public ResponseEntity<Object> handleBindingException(MethodArgumentNotValidException ex){
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}
