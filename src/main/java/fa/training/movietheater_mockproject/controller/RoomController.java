package fa.training.movietheater_mockproject.controller;

import fa.training.movietheater_mockproject.enums.AppConstant;
import fa.training.movietheater_mockproject.model.dto.MemberDto;
import fa.training.movietheater_mockproject.model.dto.RoomDetailDto;
import fa.training.movietheater_mockproject.model.dto.RoomDto;
import fa.training.movietheater_mockproject.model.entity.Cinema;
import fa.training.movietheater_mockproject.model.entity.Room;
import fa.training.movietheater_mockproject.model.entity.RoomType;
import fa.training.movietheater_mockproject.model.entity.Seat;
import fa.training.movietheater_mockproject.service.*;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@AllArgsConstructor
@RequestMapping("/admin/room")
public class RoomController {
    private final SeatService seatService;
    private final TypeSeatService typeSeatService;
    private final RoomTypeService roomTypeService;
    private final CinemaService cinemaService;
    private final RoomService roomService;
    private final ScheduleService scheduleService;

    @GetMapping
    public String showListRoom(@RequestParam("size") Optional<Integer> sizeOpt,
                               @RequestParam("page") Optional<Integer> pageOpt,
                               @RequestParam("q") Optional<String> keywordOpt,
                               Model model) {
        Integer size = sizeOpt.orElse(AppConstant.SIZE);
        Integer page = pageOpt.orElse(AppConstant.PAGE_NUMBER);
        String keyword = keywordOpt.orElse("");
        Integer offset = page * size;
        Integer totalRoom = roomService.totalRoomDetailDtoListAll().get();
        Integer totalPage = 0;
        if (totalRoom % size == 0) {
            totalPage = totalRoom / size;
        } else {
            totalPage = totalRoom / size + 1;
        }
        List<RoomDetailDto> roomDetailDtos = roomService.getRoomDetailDtoList(keyword, size, offset);
        model.addAttribute("roomDetailDtos", roomDetailDtos);

        model.addAttribute("totalPage", totalPage);
        model.addAttribute("page", page);
        model.addAttribute("size", size);
        model.addAttribute("keyword", keyword);
        return "/room/list-room";
    }

    @GetMapping("/add")
    public String showRoomForm(Model model) {
        RoomDto roomDto = new RoomDto();
        roomDto.setStatus(true);
        model.addAttribute("listCinema", cinemaService.getAllListCinema());
        model.addAttribute("listRoomType", roomTypeService.getAllListRoomType());
        model.addAttribute("roomDto", roomDto);
        return "/room/room-form";
    }

    @PostMapping("/save")
    public String saveRoom(@Valid RoomDto roomDto, BindingResult bindingResult,
                           @RequestParam(name = "roomTypeId") Optional<Long> roomTypeId,
                           @RequestParam(name = "cinemaId") Optional<Long> cinemaId,
                           Model model, RedirectAttributes redirectAttributes) {
        Cinema cinema = cinemaService.getCinemaById(cinemaId.get());
        RoomType roomType = roomTypeService.getRoomTypeById(roomTypeId.get());
        roomDto.setCinema(cinema);
        roomDto.setRoomType(roomType);
        if (bindingResult.hasErrors()) {
            model.addAttribute("listCinema", cinemaService.getAllListCinema());
            model.addAttribute("listRoomType", roomTypeService.getAllListRoomType());
            return "/room/room-form";
        }
        Room room = new Room();
        BeanUtils.copyProperties(roomDto, room);
        room.setSeatQuantity(roomType.getNumberHorizontalColumnSeats() * roomType.getNumberHorizontalRowSeats());
        roomService.saveRoom(room);
        if (roomDto.getRoomId() == null){
            for (char i = 'A'; i < 'A' + roomType.getNumberHorizontalColumnSeats() ; i++) {
                for (int j = 1; j <= roomType.getNumberHorizontalRowSeats(); j++) {
                    Seat seat = new Seat();
                    seat.setRoom(room);
                    seat.setSeatName(String.valueOf(i)+j);
                    seat.setTypeSeat(typeSeatService.getTypeSeatById(1L));
                    seatService.addSeat(seat);
                }
            }
            redirectAttributes.addFlashAttribute("result", "Add Room Success!");
        }else {
            redirectAttributes.addFlashAttribute("result", "Update Room Success!");
        }
        return "redirect:/admin/room/seat/"+ room.getRoomId();
    }

    @GetMapping("/update/{id}")
    public String updateRoom(@PathVariable(name = "id") Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Room> optionalRoom = roomService.getRoomById(id);
        if (optionalRoom.isEmpty()) {
            throw new IllegalArgumentException("Can not found entity with id: " + id);
        }
        RoomDto roomDto = new RoomDto();
        BeanUtils.copyProperties(optionalRoom.get(), roomDto);
        model.addAttribute("listCinema", cinemaService.getAllListCinema());
        model.addAttribute("listRoomType", roomTypeService.getAllListRoomType());
        model.addAttribute("roomDto", roomDto);
        return "/room/room-form";
    }

    @GetMapping("/delete/{id}")
    public String deleteRoom(@PathVariable(name = "id") Long id, RedirectAttributes redirectAttributes) {
        Optional<Room> optionalRoom = roomService.getRoomById(id);
        if (optionalRoom.isEmpty()) {
            throw new IllegalArgumentException("Can not found entity with id: " + id);
        }
        if (scheduleService.getScheduleByRoom(optionalRoom.get()).size()>0){
            redirectAttributes.addFlashAttribute("result", "Room in use cannot be deleted!");
            return "redirect:/admin/room";
        }else{
            roomService.deleteRoom(optionalRoom.get());
            redirectAttributes.addFlashAttribute("result", "Delete Room Success!");
            return "redirect:/admin/room";
        }
    }

    @GetMapping("/seat/{id}")
    public String showAddSeat(Model model,@PathVariable(name = "id") Long id){
        Optional<Room> optionalRoom = roomService.getRoomById(id);
        if (optionalRoom.isEmpty()) {
            throw new IllegalArgumentException("Can not found entity with id: " + id);
        }
        List<Character> characters = new ArrayList<>();
        for (char i = 'A'; i < 'A' + optionalRoom.get().getRoomType().getNumberHorizontalColumnSeats() ; i++) {
            characters.add(i);
        }
        model.addAttribute("seatColumns",characters);
        model.addAttribute("rows",optionalRoom.get().getRoomType().getNumberHorizontalRowSeats());
        model.addAttribute("columns",optionalRoom.get().getRoomType().getNumberHorizontalColumnSeats());
        model.addAttribute("room",optionalRoom.get());
        return "room/add-seat";
    }
}
