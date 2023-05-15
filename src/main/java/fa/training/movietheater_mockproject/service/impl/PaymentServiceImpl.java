package fa.training.movietheater_mockproject.service.impl;

import fa.training.movietheater_mockproject.model.entity.Payment;
import fa.training.movietheater_mockproject.repository.PaymentRepository;
import fa.training.movietheater_mockproject.service.PaymentService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    @Override
    public List<Payment> getAll() {
        return (List<Payment>) paymentRepository.findAll();
    }

    @Override
    public Optional<Payment> findPaymentByName(String paymentName) {
        return paymentRepository.findPaymentByPaymentNameAndDeletedFalse(paymentName);
    }
}
