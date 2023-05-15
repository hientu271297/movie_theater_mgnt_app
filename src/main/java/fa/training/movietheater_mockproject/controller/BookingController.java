package fa.training.movietheater_mockproject.controller;

import fa.training.movietheater_mockproject.enums.AppConstant;
import fa.training.movietheater_mockproject.model.dto.BookingDto;
import fa.training.movietheater_mockproject.model.entity.*;
import fa.training.movietheater_mockproject.service.*;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
public class BookingController {
    private final BookingService bookingService;
    private final BillService billService;

    private final TicketService ticketService;

    private final BillVoucherService billVoucherService;

    private final FoodOrderService foodOrderService;

    @GetMapping("/booking-list")
    String showBookingList(Model model,
                           @RequestParam("size") Optional<Integer> sizeOpt,
                           @RequestParam("page") Optional<Integer> pageOpt,
                           @RequestParam("q") Optional<String> keywordOpt) {
        Integer size = sizeOpt.orElse(AppConstant.SIZE);
        Integer page = pageOpt.orElse(AppConstant.PAGE_NUMBER);
        String keyword = keywordOpt.orElse("");
        Integer offset = page * size;
        List<BookingDto> bookingDtoList = bookingService.getBookingList(keyword, size, offset);
        for (BookingDto bookingDto : bookingDtoList) {
            Bill bill = billService.getBillById(bookingDto.getBillId()).get();
            bookingDto.setPaymentName(bill.getPayment().getPaymentName());
            bookingDto.setBillId(bill.getBillId());
            if (bill.getMember() != null && bill.getMember().getPointsCard() != null) {
                bookingDto.setPoint(bill.getMember().getPointsCard().getPoint());
            }
            bookingDto.setTotalPrice(bill.getTotalPrice());
            List<Ticket> tickets = ticketService.getTicketListByBillId(bill);
            List<Seat> seats = tickets.stream().map(s -> s.getSeat()).collect(Collectors.toList());
            if (seats != null) {
                StringBuilder seatsStr = new StringBuilder();
                for (Seat seat : seats) {
                    seatsStr.append(seat.getSeatName());
                    seatsStr.append(", ");
                }
                String newSeatsStr = seatsStr.toString();
                if (newSeatsStr.endsWith(", ")) {
                    newSeatsStr = newSeatsStr.substring(0, newSeatsStr.length() - 2);
                }
                bookingDto.setSeatsStr(newSeatsStr);
            } else {
                bookingDto.setSeatsStr("");
            }

            List<BillVoucher> billVouchers = billVoucherService.getBillVoucherByBill(bill);
            List<FoodOrder> foodOrders = foodOrderService.getAllFoodOrderByBill(bill);
            if (foodOrders != null) {
                StringBuilder foodsStr = new StringBuilder();
                for (FoodOrder fo : foodOrders) {
                    Food food = fo.getFood();
                    foodsStr.append(food.getFoodName());
                    foodsStr.append(" x ");
                    foodsStr.append(fo.getQuantity().toString());
                    foodsStr.append(", ");
                }
                String newFoodsStr = foodsStr.toString();
                if (newFoodsStr.endsWith(", ")) {
                    newFoodsStr = newFoodsStr.substring(0, newFoodsStr.length() - 2);
                }
                bookingDto.setFoodStr(newFoodsStr);
            } else {
                bookingDto.setFoodStr("");
            }

            if (billVouchers != null) {
                StringBuilder vouchersStr = new StringBuilder();
                for (BillVoucher bv : billVouchers) {
                    Voucher voucher = bv.getVoucher();
                    vouchersStr.append(voucher.getVoucherName());
                    vouchersStr.append(", ");
                }
                String newVouchersStr = vouchersStr.toString();
                if (newVouchersStr.endsWith(", ")) {
                    newVouchersStr = newVouchersStr.substring(0, newVouchersStr.length() - 2);
                }
                bookingDto.setVoucherStr(newVouchersStr);
            } else {
                bookingDto.setVoucherStr("");
            }
        }

        model.addAttribute("bookingDtoList", bookingDtoList);
        Integer totalBookingDto = bookingService.totalBooking().get();
        Integer totalPage = 0;
        if (totalBookingDto % size == 0) {
            totalPage = totalBookingDto / size;
        } else {
            totalPage = totalBookingDto / size + 1;
        }
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("page", page);
        model.addAttribute("size", size);
        model.addAttribute("keyword", keyword);

        return "booking/booking-list";
    }

    @GetMapping("/booking-movie")
    public String showMovieAndCinema() {
        return "booking/booking-movie-cinema";
    }

    @GetMapping("/pay")
    public String showPay(){
        return "booking/pay";
    }

}
