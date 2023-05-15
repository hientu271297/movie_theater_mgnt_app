package fa.training.movietheater_mockproject.service;

import fa.training.movietheater_mockproject.model.entity.Bill;
import fa.training.movietheater_mockproject.model.entity.BillVoucher;
import fa.training.movietheater_mockproject.model.entity.FoodOrder;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

public interface BillVoucherService {
    List<BillVoucher> getBillVoucherByBill(Bill bill);
}
