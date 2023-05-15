package fa.training.movietheater_mockproject.controller;

import fa.training.movietheater_mockproject.enums.AppConstant;
import fa.training.movietheater_mockproject.exception.NotAcceptable;
import fa.training.movietheater_mockproject.exception.ResourceNotFound;
import fa.training.movietheater_mockproject.model.dto.MovieWithFormatDto;
import fa.training.movietheater_mockproject.model.dto.PagingMovieWithFormat;
import fa.training.movietheater_mockproject.model.dto.ScheduleDto;
import fa.training.movietheater_mockproject.model.dto.ScheduleWrapper;
import fa.training.movietheater_mockproject.model.entity.*;
import fa.training.movietheater_mockproject.service.*;
import fa.training.movietheater_mockproject.util.ConvertUtil;
import fa.training.movietheater_mockproject.util.DateUtils;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/schedule")
@AllArgsConstructor
public class ScheduleController {
    private final MovieService movieService;
    private final RoomService roomService;
    private final CityService cityService;
    private final DateTypeService dateTypeService;
    private MovieMovieFormatService movieMovieFormatService;
    private MovieFormatService movieFormatService;
    private final CinemaService cinemaService;
    private final ScheduleService scheduleService;

    private final CategoryService categoryService;

    private final BillService billService;

    @GetMapping
    public String listCities(Model model){
        model.addAttribute("scheduleWrapper", new ScheduleWrapper());
        return "schedule/page1";
    }
    //    get all if small data
    @ResponseBody
    @GetMapping("/cities")
    public ResponseEntity<?> getCities(){
        return ResponseEntity.ok(cityService.getAll());
    }

//    recommend to get of big data
//    @ResponseBody
//    @GetMapping("/cities")
//    public ResponseEntity<?> getCities(@RequestParam(name = "cityId",required = false) Long cityId,
//                                       @RequestParam(name = "cinemaId",required = false) Long cinemaId) {
//        List<Object> data = new LinkedList<>();
//        if (cityId != null && cinemaId == null) {
//            Optional<City> city = cityService.findById(cityId);
//            if (city.isEmpty()) throw new ResourceNotFound("Have not city id: " + city);
//            List<Cinema> cinemas = cinemaService.getCinemaByCity(city.get());
//            if (cinemas.size() > 0) {
//                data.add(cinemas);
//                List<Room> rooms = roomService.findByCinema(cinemas.get(0));
//                if (rooms.size() > 0) {
//                    data.add(rooms);
//                }
//            }
//        }
//        if (cinemaId != null) {
//            Cinema cinema = cinemaService.getCinemaById(cinemaId);
//            if (cinema == null) throw new ResourceNotFound("Have not cinema id: " + cinemaId);
//            List<Room> rooms = roomService.findByCinema(cinema);
//            if (rooms.size() > 0) {
//                data.add(rooms);
//            }
//        }
//        return new ResponseEntity<>(data, HttpStatus.OK);
//    }



//    @GetMapping("/getSchedule")
//    public String showCreateSchedule(@RequestParam(name = "roomId") Long roomId,
//                                    @RequestParam(name = "date") LocalDate date
//                                    ,Model model){
//
//        List<Movie> movies = movieService.findByEarlyDate()
//    }

    @ResponseBody
    @GetMapping("/listSchedules")
    public ResponseEntity<?> getListSchedules(@RequestParam(name = "roomId",required = false) Long roomId ,
                                              @RequestParam(name = "date",required = false) String dateString){

        LocalDate date = DateUtils.stringToDate(dateString);

        Optional<Room> roomOpt = roomService.getRoomById(roomId);
        if(dateString == null || dateString.isEmpty() || date == null){
            return ResponseEntity.ok("date error");
        }

        ScheduleWrapper scheduleWrapper = new ScheduleWrapper();
        Optional<Room> room = roomService.findRoomByRoomId(roomId);
        if(room.isEmpty()) {
            throw new ResourceNotFound("can not find room with id: " + roomId);
        }

        List<Schedule> schedules = scheduleService.getSchedulesByDateAndRoom(room.get(),date);

        List<ScheduleDto> scheduleDtos = schedules.stream().map(s ->{
            ScheduleDto scheduleDto = new ScheduleDto();
            BeanUtils.copyProperties(s,scheduleDto);
            scheduleDto.setDuration(s.getMovieMovieFormat().getMovie().getDuration());
            scheduleDto.setMovieFormatId(s.getMovieMovieFormat().getMovieFormat().getMovieFormatId());
            scheduleDto.setMovieId(s.getMovieMovieFormat().getMovie().getMovieId());
            scheduleDto.setMovieName(s.getMovieMovieFormat().getMovie().getMovieName());
            scheduleDto.setImageSmallUrl(s.getMovieMovieFormat().getMovie().getImageSmallUrl());
            List<MovieFormat> movieFormats = movieFormatService.findByMovieId(s.getMovieMovieFormat().getMovie().getMovieId());
            List<Category> categories = categoryService.findByMovieId(s.getMovieMovieFormat().getMovie().getMovieId());
            scheduleDto.setMovieFormats(movieFormats);
            scheduleDto.setCategories(categories);
            Optional<Bill> billOpt = billService.findFirstBill(s);
            if(billOpt.isPresent()){
                scheduleDto.setStatusEdit(null);
            }
            return scheduleDto;
        }).collect(Collectors.toList());

        scheduleWrapper.setDate(date);
        scheduleWrapper.setRoomId(roomId);
        scheduleWrapper.setCinemaName(room.get().getCinema().getCinemaName());
        scheduleWrapper.setCityName(room.get().getCinema().getCity().getCityName());
        scheduleWrapper.setRoomName(roomOpt.get().getRoomName());
        scheduleWrapper.setScheduleDtos(scheduleDtos);
        return ResponseEntity.ok(scheduleWrapper);
    }
    @ResponseBody
    @GetMapping("/listMovies")
    public ResponseEntity<?> getListMovies(@RequestParam(name = "pageNumber",required = false) Integer pageNumber,
                                           @RequestParam(name = "size", required = false) Integer size,
                                           @RequestParam(name = "q",required = false) String keyword,
                                           @RequestParam(name = "sort",required = false) String sort){
        if(pageNumber == null) pageNumber = AppConstant.PAGE_NUMBER;
        if(size == null) size = 10;

        PageRequest page = PageRequest.of(pageNumber,size);

        Specification<Movie> undelete = (root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("deleted"),false);
        };

        Specification<Movie> spec = null;
        if(keyword!= null && !keyword.isEmpty()){

            Specification<Movie> specOr = null;
            Double aDouble = ConvertUtil.stringToDouble(keyword);
            Integer aInteger = ConvertUtil.stringToInteger(keyword);
            LocalDate date = DateUtils.stringToDate(keyword);

            if(aDouble != null){
                spec = ((root, query, criteriaBuilder) ->
                        criteriaBuilder.equal(root.get("year"),aInteger));
                specOr = spec.or(specOr);

                spec = ((root, query, criteriaBuilder) ->
                        criteriaBuilder.equal(root.get("duration"),aInteger));
                specOr = spec.or(specOr);
            }

            if(aDouble != null){
                spec = ((root, query, criteriaBuilder) ->
                        criteriaBuilder.equal(root.get("moviePrice"),aDouble));
                specOr = spec.or(specOr);
            }
            if(date != null){
                spec = ((root, query, criteriaBuilder) ->
                        criteriaBuilder.equal(root.get("earlyDate"),date));
                specOr = spec.or(specOr);

                spec = ((root, query, criteriaBuilder) ->
                        criteriaBuilder.equal(root.get("startDate"),date));
                specOr = spec.or(specOr);

                spec = ((root, query, criteriaBuilder) ->
                        criteriaBuilder.equal(root.get("earlyDate"),date));
                specOr = spec.or(specOr);
            }

            spec = ((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("actor"),"%"+keyword+"%"));
            specOr = spec.or(specOr);

            spec = ((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("movieName"),"%"+keyword+"%"));
            specOr = spec.or(specOr);

            spec = ((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("producer"),"%"+keyword+"%"));
            specOr = spec.or(specOr);

            undelete = undelete.and(specOr);
        }

        Page<Movie> moviePage = movieService.getAll(undelete,page);

        List<MovieWithFormatDto> movieWithFormatDtos = moviePage.toList().stream().map(m -> {
            MovieWithFormatDto movieWithFormatDto = new MovieWithFormatDto();
            BeanUtils.copyProperties(m, movieWithFormatDto);
            List<MovieFormat> movieFormats = m.getMovieMovieFormats().stream().map(MovieMovieFormat::getMovieFormat).collect(Collectors.toList());
            List<Category> categories = m.getMovieCategories().stream().map(MovieCategory::getCategory).collect(Collectors.toList());
            movieWithFormatDto.setMovieFormats(movieFormats);
            movieWithFormatDto.setCategories(categories);
            return movieWithFormatDto;
        }).collect(Collectors.toList());

        PagingMovieWithFormat pageMovie = new PagingMovieWithFormat();
        pageMovie.setMovieWithFormatDtos(movieWithFormatDtos);
        pageMovie.setPageNumber(pageNumber);
        pageMovie.setTotalPage(moviePage.getTotalPages());
        pageMovie.setSize(size);
        pageMovie.setKeyword(keyword);
        pageMovie.setSort(sort);
        return ResponseEntity.ok(pageMovie);
    }
    @GetMapping("/getInfo")
    public String getInfo(@RequestParam(name = "cityIndex", required = false) Long cityIndex,
                          @RequestParam(name = "cinemaIndex",required = false) Long cinemaIndex,
                          @RequestParam(name = "roomId",required = false) Long roomId ,
                          @RequestParam(name = "date",required = false) String dateString,
                          @RequestParam(name = "pageNumber",required = false) Integer pageNumber,
                          @RequestParam(name = "size", required = false) Integer size,
                          @RequestParam(name = "q",required = false) String keyword,
                          @RequestParam(name = "sort",required = false) String sort,
                          Model model){

        ScheduleWrapper scheduleWrapper = new ScheduleWrapper();
        scheduleWrapper.setCityId(cityIndex);
        scheduleWrapper.setCinemaId(cinemaIndex);
        scheduleWrapper.setRoomId(roomId);
        model.addAttribute("listCity",cityService.getAll());
        model.addAttribute("scheduleWrapper",scheduleWrapper);
        Optional<Room> roomOpt = roomService.getRoomById(roomId);

        if(roomId == null){
            model.addAttribute("errorRoomId","You must select room");
            return "schedule/page1";
        }

        if(roomOpt.isEmpty()) throw new ResourceNotFound("can not find room with id: "+roomId);

        LocalDate date = DateUtils.stringToDate(dateString);

        if(dateString == null || dateString.isEmpty() || date == null){
            model.addAttribute("errorDate", "invalid input date !!!");
            return "schedule/page1";
        }


        if(pageNumber == null) pageNumber = AppConstant.PAGE_NUMBER;
        if(size == null) size = AppConstant.SIZE;

        PageRequest page = PageRequest.of(pageNumber,size);

        Specification<Movie> undelete = (root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("deleted"),false);
        };

        Page<Movie> moviePage = movieService.getAll(undelete,page);

        List<MovieWithFormatDto> movieWithFormatDtos = moviePage.toList().stream().map(m -> {
            MovieWithFormatDto movieWithFormatDto = new MovieWithFormatDto();
            BeanUtils.copyProperties(m, movieWithFormatDto);
            List<MovieFormat> movieFormats = m.getMovieMovieFormats().stream().map(MovieMovieFormat::getMovieFormat).collect(Collectors.toList());
            List<Category> categories = m.getMovieCategories().stream().map(MovieCategory::getCategory).collect(Collectors.toList());
            movieWithFormatDto.setMovieFormats(movieFormats);
            movieWithFormatDto.setCategories(categories);
            return movieWithFormatDto;
        }).collect(Collectors.toList());

        PagingMovieWithFormat pageMovie = new PagingMovieWithFormat();
        pageMovie.setMovieWithFormatDtos(movieWithFormatDtos);
        pageMovie.setTotalPage(moviePage.getTotalPages());
        pageMovie.setPageNumber(pageNumber);
        pageMovie.setSize(size);
        pageMovie.setKeyword(keyword);
        pageMovie.setSort(sort);


//        List<Movie> movies = movieService.findByEarlyDate(date);
//
//        List<MovieWithFormatDto> movieWithFormatDtos = movies.stream().map(m->{
//            MovieWithFormatDto movieWithFormatDto = new MovieWithFormatDto();
//            BeanUtils.copyProperties(m, movieWithFormatDto);
//            List<MovieFormat> movieFormats = m.getMovieMovieFormats().stream().map(MovieMovieFormat::getMovieFormat).collect(Collectors.toList());
//            movieWithFormatDto.setMovieFormats(movieFormats);
//            return movieWithFormatDto;
//        }).collect(Collectors.toList());

        model.addAttribute("movieWithFormatDtos", movieWithFormatDtos);
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("totalPage", moviePage.getTotalPages());
        model.addAttribute("size", size);

        Optional<Room> room = roomService.findRoomByRoomId(roomId);
        if(room.isEmpty()) throw new ResourceNotFound("can not find room with id: "+roomId);

        List<Schedule> schedules = scheduleService.getSchedulesByDateAndRoom(room.get(),date);

        List<ScheduleDto> scheduleDtos = schedules.stream().map(s ->{
            ScheduleDto scheduleDto = new ScheduleDto();
            BeanUtils.copyProperties(s,scheduleDto);
            scheduleDto.setDuration(s.getMovieMovieFormat().getMovie().getDuration());
            scheduleDto.setMovieFormatId(s.getMovieMovieFormat().getMovieFormat().getMovieFormatId());
            scheduleDto.setMovieId(s.getMovieMovieFormat().getMovie().getMovieId());
            scheduleDto.setMovieName(s.getMovieMovieFormat().getMovie().getMovieName());
            scheduleDto.setImageSmallUrl(s.getMovieMovieFormat().getMovie().getImageSmallUrl());
            List<MovieFormat> movieFormats = movieFormatService.findByMovieId(s.getMovieMovieFormat().getMovie().getMovieId());
            List<Category> categories = categoryService.findByMovieId(s.getMovieMovieFormat().getMovie().getMovieId());
            scheduleDto.setMovieFormats(movieFormats);
            scheduleDto.setCategories(categories);
            Optional<Bill> billOpt = billService.findFirstBill(s);
            if(billOpt.isPresent()){
                scheduleDto.setStatusEdit(null);
            }
            return scheduleDto;
        }).collect(Collectors.toList());


        scheduleWrapper.setDate(date);
        scheduleWrapper.setCinemaName(room.get().getCinema().getCinemaName());
        scheduleWrapper.setCityName(room.get().getCinema().getCity().getCityName());
        scheduleWrapper.setRoomName(roomOpt.get().getRoomName());
        scheduleWrapper.setScheduleDtos(scheduleDtos);
        return "schedule/page1";
    }

    @PostMapping("/create")
    public String createSchedule(
            ScheduleWrapper scheduleWrapper,
            Model model,
            RedirectAttributes redirectAttributes) {

        redirectAttributes.addAttribute("date",scheduleWrapper.getDate().toString());
        redirectAttributes.addAttribute("roomId",scheduleWrapper.getRoomId());

        LocalDate date = scheduleWrapper.getDate();
        if(scheduleWrapper.getRoomId() == null || scheduleWrapper.getDate() == null){
            model.addAttribute("result","invalid information");
            return "schedule/page1";
        }

        Optional<DateType> dateType = dateTypeService.findDateTypeByDate(date);
        Optional<Room> roomOpt = roomService.getRoomById(scheduleWrapper.getRoomId());

        if(roomOpt.isEmpty()) throw new ResourceNotFound("can not find room with id: "+scheduleWrapper.getRoomId());

        if(dateType.isEmpty()) {
            DayOfWeek dayOfWeek = date.getDayOfWeek();
            if (dayOfWeek.ordinal() >= DayOfWeek.MONDAY.ordinal() && dayOfWeek.ordinal() <= DayOfWeek.FRIDAY.ordinal()) {
                dateType = dateTypeService.findDateTypeByDateName("Weekday");
            } else {
                dateType = dateTypeService.findDateTypeByDateName("Weekend");
            }
        }
        int i=0;

// FILTER ELEMENTS TO SAVE OR DELETE:
        Set<ScheduleDto> setDtoSaves = new LinkedHashSet<>();
        List<ScheduleDto> listDtoDeletes = new LinkedList<>();
        List<ScheduleDto> scheduleDtosClient = scheduleWrapper.getScheduleDtos();
        List<ScheduleDto> scheduleDtosServer = scheduleService.getSchedulesByDateAndRoom(roomOpt.get(),scheduleWrapper.getDate()).stream()
                .map(s->{
                    ScheduleDto scheduleDto = new ScheduleDto();
                    BeanUtils.copyProperties(s,scheduleDto);
                    scheduleDto.setMovieFormatId(s.getMovieMovieFormat().getMovieFormat().getMovieFormatId());
                    scheduleDto.setMovieId(s.getMovieMovieFormat().getMovie().getMovieId());
                    scheduleDto.setMovieName(s.getMovieMovieFormat().getMovie().getMovieName());
                    scheduleDto.setImageSmallUrl(s.getMovieMovieFormat().getMovie().getImageSmallUrl());
                    List<MovieFormat> movieFormats = movieFormatService.findByMovieId(s.getMovieMovieFormat().getMovie().getMovieId());
                    scheduleDto.setMovieFormats(movieFormats);

                    Optional<Bill> billOpt = billService.findFirstBill(s);
                    if(billOpt.isPresent()){
                        scheduleDto.setStatusEdit(null);
                    }
                    return scheduleDto;
                }).collect(Collectors.toList());

//  CHECK LIST TO DELETE IF LIST DATA SCHEDULES SEND FROM CLIENT IS EMPTY:
        if(scheduleWrapper.getScheduleDtos() == null){
            for (ScheduleDto sServerDelete : scheduleDtosServer) {
                Optional<Schedule> scheduleOpt = scheduleService.findScheduleById(sServerDelete.getScheduleId());
                if(scheduleOpt.isEmpty()) throw new ResourceNotFound("can not find schedule with id: "+ sServerDelete.getScheduleId());
                Optional<Bill> billOpt = billService.findFirstBill(scheduleOpt.get());
                if(billOpt.isEmpty()){
                    scheduleService.delete(scheduleOpt.get());
                }
            }
            redirectAttributes.addFlashAttribute("result","List schedules has been update");
            if(scheduleDtosServer.isEmpty()) redirectAttributes.addFlashAttribute("result","You must create schedule");
            return "redirect:/admin/schedule/getInfo";
        }

// CHECK DATA FROM CLIENT IF CONTAIN ELEMENT EMPTY:
        for (ScheduleDto scheduleDto : scheduleDtosClient) {
            if(scheduleDto.getMovieId()  == null || scheduleDto.getStartAt() == null || scheduleDto.getEndAt() == null){
                redirectAttributes.addFlashAttribute("result","time can not be empty");
                return "redirect:/admin/schedule/getInfo";
            }
        }
//  CHECK LIST DATA FROM CLIENT IF NOT EMPTY
        scheduleDtosClient = scheduleDtosClient.stream().sorted().collect(Collectors.toList());

        if(scheduleDtosClient.size()>1){
            for(int k = 0; k < scheduleDtosClient.size() - 1; k++){
                if(!scheduleDtosClient.get(k).getEndAt().plusMinutes(29).isBefore(scheduleDtosClient.get(k + 1).getStartAt())){
                    redirectAttributes.addFlashAttribute("result","invalid time setup");
                    return "redirect:/admin/schedule/getInfo";
                }
            }
        }

//  FILTER ELEMENT TO SAVE OR UPDATE OR DELETE:
        for (ScheduleDto sServer: scheduleDtosServer) {
            int save = 0;

            for(ScheduleDto sClient : scheduleDtosClient){
                if(Objects.equals(sServer.getScheduleId(), sClient.getScheduleId())){
                    save = 1;
                    Optional<Schedule> scheduleOptional = scheduleService.findScheduleById(sClient.getScheduleId());
                    if(scheduleOptional.isEmpty()) throw new ResourceNotFound("can not find schedule with id: "+ sClient.getScheduleId());
                    Optional<Bill> billOpt = billService.findFirstBill(scheduleOptional.get());
                    if(billOpt.isEmpty()){
                        setDtoSaves.add(sClient);
                    }
                } else if (sClient.getScheduleId() == null) {
                    setDtoSaves.add(sClient);
                }
            }
            if(save == 0){
                listDtoDeletes.add(sServer);
            }
        }

        for(ScheduleDto scheduleDto : setDtoSaves){
            Schedule schedule = new Schedule();
            BeanUtils.copyProperties(scheduleDto,schedule);
            schedule.setRoom(roomService.findRoomByRoomId(scheduleWrapper.getRoomId()).get());
            schedule.setMovieMovieFormat(movieMovieFormatService.
                    findMovieMovieFormatByMovieAndMovieFormat(movieService.findById(scheduleDto.getMovieId()).get(),
                            movieFormatService.findMovieFormatByMovieFormatId(scheduleDto.getMovieFormatId()).get()).get());
            schedule.setDateType(dateType.get());
            schedule.setDate(scheduleWrapper.getDate());
                scheduleService.createNew(schedule);
//            check data from client
                System.out.println("loop " + (++i) + ": ");
                System.out.println("date type is: "+dateType.get().getDateName());
                System.out.println("schedule id is: "+ scheduleDto.getScheduleId());
                System.out.println("movie id is: "+ scheduleDto.getMovieId());
                System.out.println("movie format Id is: "+ scheduleDto.getMovieFormatId());
                System.out.println("movie start at is: "+ scheduleDto.getStartAt());
                System.out.println("movie end at is: "+ scheduleDto.getEndAt());
                System.out.println("movie status edit is: "+ scheduleDto.getStatusEdit());
                System.out.println("date is: "+ scheduleWrapper.getDate());
                System.out.println("room Id is: "+ scheduleWrapper.getRoomId());
        }

        for(ScheduleDto sDelete : listDtoDeletes){
            Optional<Schedule> scheduleOpt = scheduleService.findScheduleById(sDelete.getScheduleId());
            if(scheduleOpt.isEmpty()) throw new ResourceNotFound("can not find schedule with id: "+ sDelete.getScheduleId());
            Optional<Bill> billOpt = billService.findFirstBill(scheduleOpt.get());
            if(billOpt.isEmpty()){
                scheduleService.delete(scheduleOpt.get());
            }
        }
        redirectAttributes.addAttribute("cityIndex",scheduleWrapper.getCityId());
        redirectAttributes.addAttribute("cinemaIndex",scheduleWrapper.getCinemaId());
        redirectAttributes.addFlashAttribute("result","List schedules has been update");
        return "redirect:/admin/schedule/getInfo";
    }

    @GetMapping("/delete/{scheduleId}")
    public String deleteItem(@PathVariable(name = "scheduleId") Long scheduleId,
                             @RequestParam(name = "date",required = false) String dateStr,
                             @RequestParam(name = "roomId",required = false) Long roomId,
                             RedirectAttributes redirectAttributes){
        Optional<Schedule> scheduleOpt = scheduleService.findScheduleById(scheduleId);
        if(scheduleOpt.isEmpty()) throw new ResourceNotFound("can not find schedule with id: "+scheduleId);
        Optional<Bill> billOpt = billService.findFirstBill(scheduleOpt.get());
        if(billOpt.isPresent()){
            throw new NotAcceptable("You can not DELETE this schedule with start at: "+ scheduleOpt.get().getStartAt()+" and end at: "+ scheduleOpt.get().getEndAt());
        }
        LocalDate date = DateUtils.stringToDate(dateStr);

        scheduleService.delete(scheduleOpt.get());
        if(date != null){
            redirectAttributes.addAttribute("date",date.toString());
        }
        redirectAttributes.addAttribute("roomId",roomId);
        redirectAttributes.addFlashAttribute("result", "schedule with start time: "+ scheduleOpt.get().getStartAt() + " has been deleted");
//        redirectAttributes.addFlashAttribute("result", "delete success");
        return "redirect:/admin/schedule/getInfo";
    }
}
