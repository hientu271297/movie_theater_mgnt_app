package fa.training.movietheater_mockproject.service;

import fa.training.movietheater_mockproject.model.dto.bookingdto.BillRequestDto;
import fa.training.movietheater_mockproject.model.dto.bookingdto.BillStatusDto;
import fa.training.movietheater_mockproject.model.entity.Bill;
import fa.training.movietheater_mockproject.model.entity.Schedule;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface BillService {
    Long createNewBill(BillRequestDto billRequestDto);

    List<Bill> getBillBySchedule(Schedule schedule);

    Optional<Bill> getBillById(Long billId);

    void updateStatusBill(BillStatusDto billStatusDto);


    List<Bill> getAllBillBySchedule(Schedule schedule);
    Optional<Bill> findFirstBill(Schedule schedule);
}
