package fa.training.movietheater_mockproject.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class PagingMovieWithFormat {
    private Integer pageNumber;
    private Integer size;
    private Integer totalPage;
    private String Keyword;
    private String sort;
    private List<MovieWithFormatDto> movieWithFormatDtos;
}
