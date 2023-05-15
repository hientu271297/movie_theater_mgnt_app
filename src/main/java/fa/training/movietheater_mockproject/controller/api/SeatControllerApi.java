package fa.training.movietheater_mockproject.controller.api;

import fa.training.movietheater_mockproject.model.dto.SeatDto;
import fa.training.movietheater_mockproject.model.entity.Room;
import fa.training.movietheater_mockproject.model.entity.Seat;
import fa.training.movietheater_mockproject.service.RoomService;
import fa.training.movietheater_mockproject.service.SeatService;
import fa.training.movietheater_mockproject.service.TypeSeatService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@AllArgsConstructor
@RequestMapping("/seat")
public class SeatControllerApi {
    private final RoomService roomService;
    private final SeatService seatService;
    private final TypeSeatService typeSeatService;
    
    @PostMapping("/add-seat")
    public ResponseEntity<String> saveSeat(@RequestBody List<SeatDto> seats){
        Room room = roomService.getRoomById(seats.get(1).getRoomId()).get();
        int count = 0;
        for (SeatDto seat: seats) {
            Seat seatUpdate = seatService.getSeatBySeatNameAndRoom(seat.getSeatName(),room);
            seatUpdate.setTypeSeat(typeSeatService.getTypeSeatById(seat.getSeatType()));
            seatService.addSeat(seatUpdate);
            if (seat.getSeatType() != 3L && seat.getSeatType() != 4L){
                System.out.println(seat.getSeatType());
                count++;
            }
        }
        room.setSeatQuantity(count);
        roomService.saveRoom(room);
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<Set<SeatDto>> getSeat(@RequestParam(name = "id") Long id){
        Set<Seat> seats = roomService.getRoomById(id).get().getSeats();
        Set<SeatDto> seatDtoSet = new HashSet<>();
        for (Seat seat: seats) {
            SeatDto seatDto = new SeatDto();
            seatDto.setSeatName(seat.getSeatName());
            seatDto.setSeatType(seat.getTypeSeat().getTypeSeatId());
            seatDtoSet.add(seatDto);
        }
        return ResponseEntity.ok(seatDtoSet);
    }
}
