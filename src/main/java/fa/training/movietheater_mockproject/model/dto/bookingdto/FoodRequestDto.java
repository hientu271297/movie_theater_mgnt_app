package fa.training.movietheater_mockproject.model.dto.bookingdto;

import lombok.Data;

import javax.validation.constraints.Min;

@Data
public class FoodRequestDto {
    private Long foodId;

    @Min(value = 0, message = "Quantity cannot be less than 0!!!")
    private Integer quantity;

}
