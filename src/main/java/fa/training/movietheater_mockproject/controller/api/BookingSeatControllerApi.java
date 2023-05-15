package fa.training.movietheater_mockproject.controller.api;

import fa.training.movietheater_mockproject.enums.Select;
import fa.training.movietheater_mockproject.model.dto.bookingdto.SeatSelectedDto;
import fa.training.movietheater_mockproject.model.entity.*;
import fa.training.movietheater_mockproject.service.BillService;
import fa.training.movietheater_mockproject.service.ScheduleService;
import fa.training.movietheater_mockproject.service.SeatService;
import fa.training.movietheater_mockproject.service.TicketService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
public class BookingSeatControllerApi {
    private final ScheduleService scheduleService;

    private final TicketService ticketService;
    private final BillService billService;
    private final SeatService seatService;

    @PostMapping("/api/booking-seat")
    public ResponseEntity<List<SeatSelectedDto>> displaySeat(@Valid @RequestParam(value = "scheduleId") Long scheduleId) {
        List<SeatSelectedDto> seatSelectedDtoList = new ArrayList<>();

        Schedule schedule = scheduleService.findScheduleById(scheduleId).get();

        Room room = schedule.getRoom();

        List<Bill> billList = billService.getBillBySchedule(schedule);

        List<Long> listSeatSelected = billList.stream()
                .flatMap(bill -> ticketService.getTicketListByBill(bill).stream()
                        .map(ticket -> ticket.getSeat().getSeatId())
                )
                .collect(Collectors.toList());

        //Get listSet by Schedule -> Room -> listSeat
        List<Seat> seatList = seatService.getSeatsByScheduleId(scheduleId);
        for (Seat seat : seatList) {
            SeatSelectedDto seatSelectedDto = new SeatSelectedDto();
            seatSelectedDto.setSeatId(seat.getSeatId());
            seatSelectedDto.setRoomId(room.getRoomId());
            seatSelectedDto.setRoomName(room.getRoomName());
            seatSelectedDto.setSeatName(seat.getSeatName());
            seatSelectedDto.setSeatType(seat.getTypeSeat().getTypeSeatId());
            seatSelectedDto.setNumberHorizontalRowSeats(room.getRoomType().getNumberHorizontalRowSeats());
            seatSelectedDto.setNumberHorizontalColumnSeats(room.getRoomType().getNumberHorizontalColumnSeats());
            seatSelectedDto.setStatus(!listSeatSelected.contains(seat.getSeatId()) ? Select.UN_SELECTED : Select.SELECTED);

            seatSelectedDtoList.add(seatSelectedDto);
        }
        return ResponseEntity.ok(seatSelectedDtoList);
    }
}
