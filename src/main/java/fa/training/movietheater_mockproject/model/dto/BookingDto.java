package fa.training.movietheater_mockproject.model.dto;

import fa.training.movietheater_mockproject.model.entity.Seat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class BookingDto {

    private Long billId;

    private Long memberId;

    private String employeeName;

    private String fullName;

    private String pointCardId;

    private String phone;

    private String movieName;

    private LocalDate date;

    private LocalTime startAt;

    private List<Seat> seats;

    private String seatsStr;

    private String foodStr;

    private String voucherStr;

    private String paymentName;

    private Integer point;

    private Double totalPrice;

    public BookingDto(Long billId, Long memberId, String employeeName, String fullName, String pointCardId, String phone, String movieName, LocalDate date, LocalTime startAt) {
        this.billId = billId;
        this.memberId = memberId;
        this.employeeName = employeeName;
        this.fullName = fullName;
        this.pointCardId = pointCardId;
        this.phone = phone;
        this.movieName = movieName;
        this.date = date;
        this.startAt = startAt;
    }
}
