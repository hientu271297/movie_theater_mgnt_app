package fa.training.movietheater_mockproject.service;

import fa.training.movietheater_mockproject.model.entity.Payment;

import java.util.List;
import java.util.Optional;

public interface PaymentService {
    List<Payment> getAll();

    Optional<Payment> findPaymentByName(String paymentName);
}
