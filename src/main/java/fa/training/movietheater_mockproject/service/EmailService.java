package fa.training.movietheater_mockproject.service;


import fa.training.movietheater_mockproject.model.dto.EmailDto;

public interface EmailService {
    boolean sendEmail(EmailDto emailDto);
}
