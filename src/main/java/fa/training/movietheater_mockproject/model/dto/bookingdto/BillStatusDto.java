package fa.training.movietheater_mockproject.model.dto.bookingdto;

import fa.training.movietheater_mockproject.enums.BillStatus;
import fa.training.movietheater_mockproject.enums.PointCardStatus;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class BillStatusDto {

    @NotNull(message = "Invalid Bill Id!!!")
    private Long billId;

    private String cardId;

    @NotNull(message = "You must choose the payment method!!!")
    private String paymentName;
}
