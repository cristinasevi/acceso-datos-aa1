package acceso.datos.aa1.movies.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class ReviewOutDto {
    private Long id;
    private String comment;
    private Integer rating;
    private LocalDate reviewDate;
    private Boolean recommended;
    private Boolean spoiler;
    private String username;
    private String movieTitle;
}
