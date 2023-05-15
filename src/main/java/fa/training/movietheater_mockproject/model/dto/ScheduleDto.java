package fa.training.movietheater_mockproject.model.dto;

import fa.training.movietheater_mockproject.model.entity.Category;
import fa.training.movietheater_mockproject.model.entity.MovieFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.w3c.dom.stylesheets.LinkStyle;

import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

@Data
public class ScheduleDto implements Comparable<ScheduleDto> {
    private Long scheduleId;
    private Long movieId;
    private String movieName;
    private Integer duration;
    private String imageSmallUrl;
    private Long movieFormatId;
    private List<MovieFormat> movieFormats;
    private List<Category> categories;
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime startAt;
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime endAt;
    private String statusEdit = "AVAILABLE EDIT";

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScheduleDto that = (ScheduleDto) o;
        return Objects.equals(scheduleId, that.scheduleId) && Objects.equals(movieId, that.movieId) && Objects.equals(movieName, that.movieName) && Objects.equals(duration, that.duration) && Objects.equals(imageSmallUrl, that.imageSmallUrl) && Objects.equals(movieFormatId, that.movieFormatId) && Objects.equals(movieFormats, that.movieFormats) && Objects.equals(categories, that.categories) && Objects.equals(startAt, that.startAt) && Objects.equals(endAt, that.endAt) && Objects.equals(statusEdit, that.statusEdit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(scheduleId, movieId, movieName, duration, imageSmallUrl, movieFormatId, movieFormats, categories, startAt, endAt, statusEdit);
    }

    @Override
    public int compareTo(ScheduleDto o) {
        return this.startAt.compareTo(o.startAt);
    }
}
