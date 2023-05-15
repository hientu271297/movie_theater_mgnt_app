package fa.training.movietheater_mockproject.model.dto.bookingdto;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class BillRequestDto {
    @NotNull(message = "Invalid Movie format Id!!!")
    private Long movieFormatId;
    @NotNull(message = "Invalid Movie Id!!!")
    private Long movieId;
    @NotNull(message = "Invalid Schedule Id!!!")
    private Long scheduleId;

    @NotNull(message = "Invalid Seat Id!!!")
    private List<Long> listSeatIds;

    @Valid
    private List<FoodRequestDto> foodRequestDtoList;

}
