package fa.training.movietheater_mockproject.service.impl;

import fa.training.movietheater_mockproject.model.entity.Bill;
import fa.training.movietheater_mockproject.model.entity.BillVoucher;
import fa.training.movietheater_mockproject.model.entity.FoodOrder;
import fa.training.movietheater_mockproject.repository.BillVoucherRepository;
import fa.training.movietheater_mockproject.service.BillVoucherService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class BillVoucherServiceImpl implements BillVoucherService {
    private final BillVoucherRepository billVoucherRepository;

    @Override
    public List<BillVoucher> getBillVoucherByBill(Bill bill) {
        return billVoucherRepository.findAllByBill(bill);
    }
}
