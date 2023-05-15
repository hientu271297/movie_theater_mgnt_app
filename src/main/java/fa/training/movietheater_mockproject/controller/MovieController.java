package fa.training.movietheater_mockproject.controller;

import fa.training.movietheater_mockproject.enums.AppConstant;
import fa.training.movietheater_mockproject.model.dto.MovieParamDto;
import fa.training.movietheater_mockproject.model.entity.*;
import fa.training.movietheater_mockproject.security.SecurityUtil;
import fa.training.movietheater_mockproject.service.*;
import fa.training.movietheater_mockproject.util.ConvertUtil;
import fa.training.movietheater_mockproject.util.DateUtils;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
@RequestMapping("/movie")
public class MovieController {

    private final MovieService movieService;

    private final EmployeeService employeeService;
    private  final CategoryService categoryService;
    private final MovieCategoryService movieCategoryService;

    private final MovieFormatService movieFormatService;

    private final MovieMovieFormatService movieMovieFormatService;

    private FileService fileService;

    private final ScheduleService scheduleService;

    @GetMapping("/add")
    String showAddMovie(Model model) {
        MovieParamDto movieParamDto = new MovieParamDto();
        model.addAttribute("movieParamDto", movieParamDto);

        model.addAttribute("categoryChooseList", Collections.emptyList());
        model.addAttribute("movieFormatChooseList", Collections.emptyList());

        List<Category> categoryListAll = categoryService.getAllCategory();
        model.addAttribute("categoryListAll", categoryListAll);

        List<MovieFormat> movieFormatList = movieFormatService.getAllMovieFormat();
        model.addAttribute("movieFormatList", movieFormatList);
        return "movie/movie-add";
    }

    @PostMapping("/add")
    String showAddMovie(Model model, @Valid MovieParamDto movieParamDto, BindingResult bindingResult, RedirectAttributes redirectAttributes,
                        @RequestParam(value = "categories", required = false) String[] categories,
                        @RequestParam(value = "movieFormats", required = false) String[] movieFormats) throws IOException {
        List<Category> categoryListAll = categoryService.getAllCategory();
        model.addAttribute("categoryListAll", categoryListAll);

        List<MovieFormat> movieFormatList = movieFormatService.getAllMovieFormat();
        model.addAttribute("movieFormatList", movieFormatList);

        if (categories == null) {
            model.addAttribute("categoryChooseList", Collections.emptyList());
            categories = Collections.emptyList().toArray(new String[0]);
        } else {
            List<Category> categoryChooseList = Arrays.stream(categories).map(s -> categoryService.getCategoryByName(s)).collect(Collectors.toList());
            model.addAttribute("categoryChooseList", categoryChooseList);
        }
        if (movieFormats == null) {
            model.addAttribute("movieFormatChooseList", Collections.emptyList());
            movieFormats = Collections.emptyList().toArray(new String[0]);
        } else {
            List<MovieFormat> movieFormatChooseList = Arrays.stream(movieFormats).map(s -> movieFormatService.findMovieFormatByName(s)).collect(Collectors.toList());
            model.addAttribute("movieFormatChooseList", movieFormatChooseList);
        }

        if (!movieParamDto.getTrailer().isBlank()) {
            if (!isValidURL(movieParamDto.getTrailer())) {
                bindingResult.rejectValue("trailer", "error.url.invalid");
            }
        }

        if ( movieParamDto.getEndDate() != null && movieParamDto.getStartDate() != null){
            if ( movieParamDto.getEndDate().isBefore(movieParamDto.getStartDate())) {
                bindingResult.rejectValue("endDate", "error.endDate.invalid");
                bindingResult.rejectValue("startDate", "error.startDate.invalid");
            }
        }

        if (bindingResult.hasErrors()) {
            return "movie/movie-add";
        }

        Movie movie = new Movie();
        BeanUtils.copyProperties(movieParamDto, movie);
        if (movieParamDto.getEarlyDate() == null){
            movie.setEarlyDate(movie.getStartDate());
        }

        movieService.create(movie);
        if (movieParamDto.getImageSmall() != null &&
                StringUtils.hasText(movieParamDto.getImageSmall().getOriginalFilename())) {
            String movieSmallFolder = "imageSmall" + movie.getMovieId();
            String imageSmallUrl = fileService.saveFile(movieParamDto.getImageSmall(), movieSmallFolder);
            movie.setImageSmallUrl(imageSmallUrl);
        }

        if (movieParamDto.getImageLarge() != null &&
                StringUtils.hasText(movieParamDto.getImageLarge().getOriginalFilename())) {
            String movieLargeFolder = "imageLarge" + movie.getMovieId();
            String imageLargeUrl = fileService.saveFile(movieParamDto.getImageLarge(), movieLargeFolder);
            movie.setImageLargeUrl(imageLargeUrl);
        }

        for (String categoryName : categories) {
            Category category = categoryService.getCategoryByName(categoryName);
            MovieCategory movieCategory = new MovieCategory();
            movieCategory.setCategory(category);
            movieCategory.setMovie(movie);
            movieCategoryService.createMovieCategory(movieCategory);
        }

        for (String format : movieFormats) {
            MovieMovieFormat movieMovieFormat = new MovieMovieFormat();
            MovieFormat movieFormat = movieFormatService.findMovieFormatByName(format);
            movieMovieFormat.setMovieFormat(movieFormat);
            movieMovieFormat.setMovie(movie);
            movieMovieFormat.setMovieMovieFormatPrice(movie.getMoviePrice() + movieFormat.getMovieFormatPrice());
            movieMovieFormatService.createMovieMovieFormat(movieMovieFormat);
        }
        redirectAttributes.addFlashAttribute("result", "Add Movie Success");
        return "redirect:/movie";
    }

    @GetMapping
    public String listMovie(@RequestParam(name = "pageNumber") Optional<Integer> pageNumberOpt,
                            @RequestParam(name = "size") Optional<Integer> sizeOpt,
                            @RequestParam(name = "q") Optional<String> keywordOpt,
                            @RequestParam(name = "filter") Optional<String> filterOpt,
                            @RequestParam(name = "sort")Optional<List<String>> sortsOpt,
                            Model model,
                            HttpServletRequest http){



        int pageNumber = pageNumberOpt.orElse(AppConstant.PAGE_NUMBER);
        int size = sizeOpt.orElse(AppConstant.SIZE);
        String keyword = keywordOpt.orElse("");
        String filter = filterOpt.orElse("");

        Specification<Movie> specUndelete = movieService.specSearch(keyword, filter);
        List<String> sorts = sortsOpt.orElseGet(() -> Arrays.asList());


        List<Sort.Order> orders = sorts.stream()
                .filter(s -> !s.isEmpty() && !s.equalsIgnoreCase("null"))
                .map(s -> s.startsWith("-") ? Sort.Order.desc(s.substring(1)) : Sort.Order.asc(s))
                .collect(Collectors.toList());

        model.addAttribute("sorts", sorts);

        PageRequest page = PageRequest.of(pageNumber,size).withSort(Sort.by(orders));
        Page<Movie> moviePage = movieService.getAll(specUndelete,page);
        model.addAttribute("keyword",keyword);
        model.addAttribute("movieList",moviePage.toList());
        model.addAttribute("totalPage",moviePage.getTotalPages());
        model.addAttribute("pageNumber",pageNumber);
        model.addAttribute("size",size);
        model.addAttribute("filter",filter);

        return "/movie/list-movie";
    }

    @GetMapping("/update/{id}")
    public String showUpdateMovie(@PathVariable Long id, Model model) {
        List<Category> categoryListAll = categoryService.getAllCategory();
        model.addAttribute("categoryListAll", categoryListAll);

        List<MovieFormat> movieFormatList = movieFormatService.getAllMovieFormat();
        model.addAttribute("movieFormatList", movieFormatList);

        Optional<Movie> movie = movieService.findById(id);
        if (movie.isEmpty()) {
            throw new IllegalArgumentException("Can not found entity with id: " + id);
        }

        List<Category> categoryChooseList = movie.get().getMovieCategories().stream().map(s -> s.getCategory()).collect(Collectors.toList());
        model.addAttribute("categoryChooseList", categoryChooseList);

        List<MovieFormat> movieFormatChooseList = movie.get().getMovieMovieFormats().stream().map(s -> s.getMovieFormat()).collect(Collectors.toList());
        model.addAttribute("movieFormatChooseList", movieFormatChooseList);

        MovieParamDto movieParamDto = new MovieParamDto();
        BeanUtils.copyProperties(movie.get(), movieParamDto);
        model.addAttribute("movieParamDto", movieParamDto);

        // check movie in schedule
        Boolean checkMovieInSchedule = false;
        List<MovieMovieFormat> movieMovieFormats = movieMovieFormatService.getMovieMovieFormatsByMovie(movie.get());
        for (MovieMovieFormat mmf : movieMovieFormats) {
            List<Schedule> schedules = scheduleService.getScheduleWithMovieAndMovieFormat(mmf);
            if(!schedules.isEmpty()){
                checkMovieInSchedule = true;
                break;
            }
        }
        model.addAttribute("checkMovieInSchedule",checkMovieInSchedule);

        return "movie/movie-add";
    }

    @PostMapping("/update/{id}")
    public String showUpdateMovie(Model model,
                                  @Valid MovieParamDto movieParamDto,
                                  BindingResult bindingResult, RedirectAttributes redirectAttributes,
                                  @PathVariable Long id,
                                  @RequestParam(value = "categories", required = false) String[] categories,
                                  @RequestParam(value = "movieFormats", required = false) String[] movieFormats) throws IOException {

        // check movie in schedule
        Movie movie = movieService.findById(id).get();
        Boolean checkMovieInSchedule = false;
        List<MovieMovieFormat> movieMovieFormats = movieMovieFormatService.getMovieMovieFormatsByMovie(movie);
        for (MovieMovieFormat mmf : movieMovieFormats) {
            List<Schedule> schedules = scheduleService.getScheduleWithMovieAndMovieFormat(mmf);
            if(!schedules.isEmpty()){
                checkMovieInSchedule = true;
                break;
            }
        }
        model.addAttribute("checkMovieInSchedule",checkMovieInSchedule);

        List<Category> categoryListAll = categoryService.getAllCategory();
        model.addAttribute("categoryListAll", categoryListAll);

        List<MovieFormat> movieFormatList = movieFormatService.getAllMovieFormat();
        model.addAttribute("movieFormatList", movieFormatList);

        if (categories == null) {
            model.addAttribute("categoryChooseList", Collections.emptyList());
            categories = Collections.emptyList().toArray(new String[0]);
        } else {
            List<Category> categoryChooseList = Arrays.stream(categories).map(s -> categoryService.getCategoryByName(s)).collect(Collectors.toList());
            model.addAttribute("categoryChooseList", categoryChooseList);
        }
        if (movieFormats == null) {
            model.addAttribute("movieFormatChooseList", Collections.emptyList());
            movieFormats = Collections.emptyList().toArray(new String[0]);
        } else {
            List<MovieFormat> movieFormatChooseList = Arrays.stream(movieFormats).map(s -> movieFormatService.findMovieFormatByName(s)).collect(Collectors.toList());
            model.addAttribute("movieFormatChooseList", movieFormatChooseList);
        }

        if (!isValidURL(movieParamDto.getTrailer())) {
            bindingResult.rejectValue("trailer", "error.url.invalid");
        }
        if (bindingResult.hasErrors()) {
            return "movie/movie-add";
        }
        if ( movieParamDto.getEndDate() != null && movieParamDto.getStartDate() != null){
            if ( movieParamDto.getEndDate().isBefore(movieParamDto.getStartDate())) {
                bindingResult.rejectValue("endDate", "error.endDate.invalid");
                bindingResult.rejectValue("startDate", "error.startDate.invalid");
            }
        }

        if (bindingResult.hasErrors()) {
            return "movie/movie-add";
        }

//        Movie movie = movieService.findById(id).get();
        List<MovieCategory> movieCategories = movieCategoryService.getMovieCategoriesByMovie(movie);
        movieCategories.forEach(e -> movieCategoryService.deleteByMovieCategoryId(e.getMovieCategoryId()));
//        List<MovieMovieFormat> movieMovieFormats = movieMovieFormatService.getMovieMovieFormatsByMovie(movie);
        if (!checkMovieInSchedule) {
            movieMovieFormats.forEach(e -> movieMovieFormatService.deleteMovieMovieFormatById(e.getMovieMovieFormatId()));
        }

        BeanUtils.copyProperties(movieParamDto, movie, "movieId", "earlyDate", "imageSmallUrl", "imageLargeUrl");

        if (movieParamDto.getImageSmall() != null &&
                StringUtils.hasText(movieParamDto.getImageSmall().getOriginalFilename())) {
            String movieSmallFolder = "imageSmall" + movie.getMovieId();
            String imageSmallUrl = fileService.saveFile(movieParamDto.getImageSmall(), movieSmallFolder);
            movie.setImageSmallUrl(imageSmallUrl);
        }

        if (movieParamDto.getImageLarge() != null &&
                StringUtils.hasText(movieParamDto.getImageLarge().getOriginalFilename())) {
            String movieLargeFolder = "imageLarge" + movie.getMovieId();
            String imageLargeUrl = fileService.saveFile(movieParamDto.getImageLarge(), movieLargeFolder);
            movie.setImageLargeUrl(imageLargeUrl);
        }

        for (String categoryName : categories) {
            Category category = categoryService.getCategoryByName(categoryName);
            MovieCategory movieCategory = new MovieCategory();
            movieCategory.setCategory(category);
            movieCategory.setMovie(movie);
            movieCategoryService.createMovieCategory(movieCategory);
        }
        if (!checkMovieInSchedule) {
            for (String format : movieFormats) {
                MovieMovieFormat movieMovieFormat = new MovieMovieFormat();
                MovieFormat movieFormat = movieFormatService.findMovieFormatByName(format);
                movieMovieFormat.setMovieFormat(movieFormat);
                movieMovieFormat.setMovie(movie);
                movieMovieFormat.setMovieMovieFormatPrice(movie.getMoviePrice() + movieFormat.getMovieFormatPrice());
                movieMovieFormatService.createMovieMovieFormat(movieMovieFormat);
            }
        }

        movieService.update(movie);
        redirectAttributes.addFlashAttribute("result", "Update Movie Success");
        return "redirect:/movie";

    }

    @GetMapping("delete/{id}")
    public String deleteMovie(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Movie movie = movieService.findById(id).get();
        movieService.delete(movie);
        redirectAttributes.addFlashAttribute("messageSuccess", "Delete Success");
        return "redirect:/movie";
    }

    public static boolean isValidURL(String urlString) {
        try {
            new URL(urlString);
            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }

}
