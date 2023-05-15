package fa.training.movietheater_mockproject.controller.api;

import fa.training.movietheater_mockproject.model.dto.bookingdto.FoodDto;
import fa.training.movietheater_mockproject.model.dto.bookingdto.FoodRequestDto;
import fa.training.movietheater_mockproject.model.dto.bookingdto.MovieCinemaDto;
import fa.training.movietheater_mockproject.model.dto.bookingdto.ScheduleDto;
import fa.training.movietheater_mockproject.model.entity.Food;
import fa.training.movietheater_mockproject.service.FoodService;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
public class BookingFoodControllerApi {
    FoodService foodService;

    @GetMapping("/api/booking-food")
    public ResponseEntity<List<FoodDto>> getListFood() {
        List<Food> foodList = foodService.getAllFood();
        List<FoodDto> foodDtoList = foodList.stream()
                .map(food -> {
                    FoodDto foodDto = new FoodDto();
                    BeanUtils.copyProperties(food, foodDto);
                    return foodDto;
                }).collect(Collectors.toList());
        return ResponseEntity.ok(foodDtoList);
    }
}
