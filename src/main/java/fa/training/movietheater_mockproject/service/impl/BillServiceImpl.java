package fa.training.movietheater_mockproject.service.impl;

import fa.training.movietheater_mockproject.enums.BillStatus;
import fa.training.movietheater_mockproject.enums.Rank;
import fa.training.movietheater_mockproject.model.dto.bookingdto.BillRequestDto;
import fa.training.movietheater_mockproject.model.dto.bookingdto.BillStatusDto;
import fa.training.movietheater_mockproject.model.dto.bookingdto.FoodRequestDto;
import fa.training.movietheater_mockproject.model.entity.*;
import fa.training.movietheater_mockproject.repository.*;
import fa.training.movietheater_mockproject.security.SecurityUtil;
import fa.training.movietheater_mockproject.model.entity.Bill;
import fa.training.movietheater_mockproject.model.entity.Schedule;
import fa.training.movietheater_mockproject.repository.BillRepository;
import fa.training.movietheater_mockproject.service.BillService;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BillServiceImpl implements BillService {

    private final BillRepository billRepository;

    private final MemberRepository memberRepository;

    private final PaymentRepository paymentRepository;

    private final ScheduleRepository scheduleRepository;

    private final EmployeeRepository employeeRepository;

    private final MovieRepository movieRepository;

    private final MovieFormatRepository movieFormatRepository;

    private final SeatRepository seatRepository;

    private final FoodRepository foodRepository;
    private final TicketRepository ticketRepository;

    private final FoodOrderRepository foodOrderRepository;
    private final PointsCardRepository pointsCardRepository;

    @Override
    @Transactional
    public Long createNewBill(BillRequestDto billRequestDto) {

        MovieFormat movieFormat = movieFormatRepository.findByMovieFormatId(billRequestDto.getMovieFormatId()).orElseThrow();

        Movie movie = movieRepository.findByMovieIdAndDeletedFalse(billRequestDto.getMovieId()).orElseThrow();

        Schedule schedule = scheduleRepository.findScheduleByScheduleIdAndDeletedFalse(billRequestDto.getScheduleId()).orElseThrow();

        //Từ billRequest lấy ra listIdSeat -> Get seatList
        List<Long> listSeatIds = billRequestDto.getListSeatIds();
        List<Seat> seatList = listSeatIds.stream()
                .map(seatId -> seatRepository.findBySeatIdAndDeletedFalse(seatId).get())
                .collect(Collectors.toList());

        //Get employee
        String currentEmployee = SecurityUtil.getCurrentUserLogin()
                .orElseThrow(() -> new NullPointerException("Not found email Employee"));

        Employee employee = employeeRepository.findByEmailAndDeletedFalse(currentEmployee).get();

        // TotalMoviePrice = movieFormat + moviePrice
        Double priceMovie = movieFormat.getMovieFormatPrice() + movie.getMoviePrice();

        Double priceDateType = schedule.getDateType().getExtraPrice();

        Double priceRoomType = scheduleRepository.getRoomTypePrice(billRequestDto.getScheduleId());

        Double listPriceSeatType = seatList.stream()
                .mapToDouble(seat -> seat.getTypeSeat().getTypeSeatPrice())
                .sum();

        Double totalPriceBill = (priceMovie + priceDateType + listPriceSeatType + priceRoomType) * seatList.size();

        //Create bill
        Bill bill = new Bill();
        bill.setTime(LocalDateTime.now());
        bill.setEmployee(employee);
        bill.setSchedule(schedule);
        bill.setDeleted(false);
        bill.setTotalPrice(totalPriceBill);
        bill.setBillPaymentStatus(BillStatus.UN_PAID);
        bill.setTook(false);
        Bill createBill = billRepository.save(bill);

        //Create ticket
        for (Seat seat : seatList) {
            if (ticketRepository.findTicketByScheduleAndSeat(schedule.getScheduleId(), seat.getSeatId()).isPresent()) {
                throw new RuntimeException("Someone was quicker to book a seat, please choose again!");
            }
            Ticket ticket = new Ticket();
            ticket.setBill(createBill);
            ticket.setSeat(seat);
            ticket.setDeleted(false);
            Double totalTicketActual = priceMovie + priceDateType + seat.getTypeSeat().getTypeSeatPrice() + priceRoomType;
            ticket.setActualPrice(totalTicketActual);
            ticketRepository.save(ticket);
        }

        List<FoodRequestDto> foodRequests = billRequestDto.getFoodRequestDtoList();

        //Get idFood and Quantity from foodRequest -> Add to Map
        Map<Long, Integer> foodMapList = foodRequests.stream()
                .filter(foodRequestDto -> foodRequestDto.getQuantity() != null && foodRequestDto.getQuantity() != 0)
                .collect(Collectors.toMap(FoodRequestDto::getFoodId, FoodRequestDto::getQuantity));


        //Nếu mapList khác null => lấy ra các Food có trong map gán vào foodList
        //Sau đó cộng tiền của bill.
        if (!foodMapList.isEmpty()) {
            List<Food> foodList = (List<Food>) foodRepository.findAllById(foodMapList.keySet());
            List<FoodOrder> foodOrders = new ArrayList<>();
            for (Food food : foodList) {
                FoodOrder foodOrder = new FoodOrder();
                foodOrder.setBill(createBill);
                foodOrder.setFood(food);
                Integer quantity = foodMapList.get(food.getFoodId());
                Double priceFood = food.getFoodPrice() * quantity;
                createBill.setTotalPrice(createBill.getTotalPrice() + priceFood);
                foodOrder.setQuantity(quantity);
                foodOrders.add(foodOrder);
                foodOrderRepository.save(foodOrder);
            }
        }

        return createBill.getBillId();
    }

    @Override
    public List<Bill> getBillBySchedule(Schedule schedule) {
        return billRepository.findByScheduleEqualsAndDeletedFalse(schedule);
    }

    @Override
    public Optional<Bill> getBillById(Long billId) {
        return billRepository.findById(billId);
    }

    @Override
    public void updateStatusBill(BillStatusDto billStatusDto) {

        Bill bill = billRepository.findById(billStatusDto.getBillId()).orElseThrow();

        Payment payment = paymentRepository.findPaymentByPaymentNameAndDeletedFalse(billStatusDto.getPaymentName().trim())
                .orElseThrow(() -> new NoSuchElementException("Not found payment method"));

        //TH1: CASH
        if (payment.getPaymentName().equalsIgnoreCase("Cash")){
            //Không nhập CardId => Thanh toán luôn, không tích điểm.
            if (billStatusDto.getCardId() == null || billStatusDto.getCardId() == "".trim()){
                bill.setBillPaymentStatus(BillStatus.PAID);
                bill.setTook(true);
                bill.setPayment(payment);
                billRepository.save(bill);

            }else {
            //Nhập Card: Đúng -> Tích điểm , Sai -> Throw Lỗi
                // Không tìm thấy phone => Báo lỗi,
                Optional<PointsCard> pointsCard = pointsCardRepository.findPointsCardByPointCardIdAndDeletedFalse(billStatusDto.getCardId().trim());
                if (pointsCard.isEmpty()) {
                    throw new RuntimeException("Not found Your Card!");
                }
                Member member = memberRepository.findMemberByPointsCardAndDeletedFalse(pointsCard.get()).orElseThrow();

                Double experience = bill.getTotalPrice();
                Integer convertExperience = Math.toIntExact(Math.round(experience));
                pointsCard.get().setPoint(pointsCard.get().getPoint() + convertExperience / 5);
                pointsCard.get().setExperience(pointsCard.get().getExperience() + convertExperience);
                pointsCardRepository.save(pointsCard.get());

                if (pointsCard.get().getExperience()>=400000){
                    pointsCard.get().setRanks(Rank.SILVER);
                }else if (pointsCard.get().getExperience()>=1000000)
                {
                    pointsCard.get().setRanks(Rank.GOLD);
                }

                bill.setMember(member);
                bill.setPayment(payment);
                bill.setBillPaymentStatus(BillStatus.PAID);
                bill.setTook(true);
                billRepository.save(bill);
            }

        }

        //TH2: CARD
        if (payment.getPaymentName().equalsIgnoreCase("PointCard")) {
            Optional<PointsCard> pointsCard = pointsCardRepository.findPointsCardByPointCardIdAndDeletedFalse(billStatusDto.getCardId().trim());
            if (pointsCard.isEmpty()) {
                throw new RuntimeException("Not found Your Card!");
            } else {
                Member member = memberRepository.findMemberByPointsCardAndDeletedFalse(pointsCard.get()).orElseThrow();
                if (pointsCard.get().getPoint() >= bill.getTotalPrice()) {

                    Double lastPoint = pointsCard.get().getPoint() - bill.getTotalPrice() + (bill.getTotalPrice() / 5);
                    Integer experience = Math.toIntExact(Math.round(bill.getTotalPrice()));
                    Integer lastPointConvertInt = Math.toIntExact(Math.round(lastPoint));
                    pointsCard.get().setPoint(lastPointConvertInt);
                    pointsCard.get().setExperience(pointsCard.get().getExperience() +  experience);
                } else {
                    throw new RuntimeException("Points are not enough to pay!");
                }

                if (pointsCard.get().getExperience()>=400000){
                    pointsCard.get().setRanks(Rank.SILVER);
                }else if (pointsCard.get().getExperience()>=1000000)
                {
                    pointsCard.get().setRanks(Rank.GOLD);
                }

                pointsCardRepository.save(pointsCard.get());
                bill.setMember(member);
                bill.setBillPaymentStatus(BillStatus.PAID);
                bill.setTook(true);
                bill.setPayment(payment);
                billRepository.save(bill);
            }
        }


    }

    //CronJob
    @Scheduled(fixedDelay = 30000)
    public void scheduleDeleteBillNotPayment() {
        List<Bill> billList =  billRepository.getAllByDeletedFalse();

        billList.stream()
                .filter(bill -> bill.getBillPaymentStatus().equals(BillStatus.UN_PAID))
                .filter(bill -> ChronoUnit.SECONDS.between(bill.getCreatedDate(), LocalDateTime.now()) > 120)
                .forEach(billRepository::delete);
    }

    @Override
    public List<Bill> getAllBillBySchedule(Schedule schedule) {
        return billRepository.getBillsBySchedule(schedule);
    }
    @Override
    public Optional<Bill> findFirstBill(Schedule schedule) {
        return billRepository.findFirstByScheduleEqualsAndDeletedFalse(schedule);
    }
}
