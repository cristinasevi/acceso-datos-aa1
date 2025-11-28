package acceso.datos.aa1.movies;

import acceso.datos.aa1.movies.domain.Movie;
import acceso.datos.aa1.movies.dto.MovieOutDto;
import acceso.datos.aa1.movies.repository.MovieRepository;
import acceso.datos.aa1.movies.service.MovieService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MovieServiceTest {

    @InjectMocks
    private MovieService movieService;

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private ModelMapper modelMapper;

    @Test
    public void testFindAll() {
        List<Movie> mockMovieList = List.of(
                new Movie(1L, "Catch Me If You Can", "A mind-bending thriller", LocalDate.of(2003, 1, 24), 141, "Action", 8.1f, "http://image1.jpg", null, null, null, null),
                new Movie(2L, "The Matrix", "A hacker discovers reality", LocalDate.of(1999, 3, 31), 136, "Science Fiction", 8.7f, "http://image2.jpg", null, null, null, null)
        );
        List<MovieOutDto> modelMapperOut = List.of(
                new MovieOutDto(1L, "Catch Me If You Can", "A mind-bending thriller", "Action", 8.1f, "http://image1.jpg"),
                new MovieOutDto(2L, "The Matrix", "A hacker discovers reality", "Science Fiction", 8.7f, "http://image2.jpg")
        );

        when(movieRepository.findAll()).thenReturn(mockMovieList);
        when(modelMapper.map(mockMovieList, new TypeToken<List<MovieOutDto>>() {}.getType())).thenReturn(modelMapperOut);

        List<MovieOutDto> actualMovieList = movieService.findAll(null, null, null, null);
        assertEquals(2, actualMovieList.size());
        assertEquals("Catch Me If You Can", actualMovieList.getFirst().getTitle());
        assertEquals("The Matrix", actualMovieList.getLast().getTitle());

        verify(movieRepository, times(1)).findAll();
        verify(movieRepository, times(0)).findByGenre("Action");
    }

    @Test
    public void testFindAllByGenre() {
        List<Movie> mockMovieList = List.of(
                new Movie(1L, "Catch Me If You Can", "A mind-bending thriller", LocalDate.of(2003, 1, 24), 141, "Action", 8.1f, "http://image1.jpg", null, null, null, null),
                new Movie(2L, "The Matrix", "A hacker discovers reality", LocalDate.of(1999, 3, 31), 132, "Science Fiction", 8.7f, "http://image2.jpg", null, null, null, null)
        );
        List<MovieOutDto> mockModelMapperOut = List.of(
                new MovieOutDto(1L, "Catch Me If You Can", "A mind-bending thriller", "Action", 8.1f, "http://image1.jpg"),
                new MovieOutDto(2L, "The Matrix", "A hacker discovers reality", "Science Fiction", 8.7f, "http://image2.jpg")
        );

        when(movieRepository.findByGenre("Action")).thenReturn(mockMovieList);
        when(modelMapper.map(mockMovieList, new TypeToken<List<MovieOutDto>>() {}.getType())).thenReturn(mockModelMapperOut);

        List<MovieOutDto> actualMovieList = movieService.findAll("Action", null, null, null);
        assertEquals(2, actualMovieList.size());
        assertEquals("Catch Me If You Can", actualMovieList.getFirst().getTitle());
        assertEquals("The Matrix", actualMovieList.getLast().getTitle());

        verify(movieRepository, times(0)).findAll();
        verify(movieRepository, times(1)).findByGenre("Action");
    }
}
