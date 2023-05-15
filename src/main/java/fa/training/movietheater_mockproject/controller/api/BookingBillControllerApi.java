package fa.training.movietheater_mockproject.controller.api;


import fa.training.movietheater_mockproject.model.dto.bookingdto.BillRequestDto;
import fa.training.movietheater_mockproject.model.dto.bookingdto.BillResponseDto;
import fa.training.movietheater_mockproject.model.dto.bookingdto.BillStatusDto;
import fa.training.movietheater_mockproject.model.dto.bookingdto.ScheduleDto;
import fa.training.movietheater_mockproject.model.entity.Bill;
import fa.training.movietheater_mockproject.service.BillService;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@AllArgsConstructor
public class BookingBillControllerApi {

    private final BillService billService;

    @PostMapping("/api/booking-bill")
    public ResponseEntity<BillResponseDto> createNewBill(@RequestBody @Valid BillRequestDto billRequestDto) {


        BillResponseDto billResponseDto = new BillResponseDto();

        Long billId = billService.createNewBill(billRequestDto);

        Bill bill = billService.getBillById(billId).get();

        BeanUtils.copyProperties(bill, billResponseDto);

        return new ResponseEntity<>(billResponseDto, HttpStatus.OK);
    }

    @PostMapping("/api/booking-bill-status")
    public ResponseEntity<String> updateBill(@RequestBody @Valid BillStatusDto billStatusDto) {

        billService.updateStatusBill(billStatusDto);

        return new ResponseEntity<>("You have successfully paid!", HttpStatus.OK);
    }

}
