package acceso.datos.aa1.movies.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class MovieOutDto {
    private Long id;
    private String title;
    private String synopsis;
    private String genre;
    private Float averageRating;
    private String imageUrl;
}
