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
                createMockMovie(1, "Catch Me If You Can", "Action"),
                createMockMovie(2, "The Matrix", "Science Fiction")
        );
        List<MovieOutDto> modelMapperOut = List.of(
                new MovieOutDto(1L, "Catch Me If You Can", "A mind-bending thriller", "Action", 8.1f, "http://image1.jpg"),
                new MovieOutDto(2L, "The Matrix", "A hacker discovers reality", "Science Fiction", 8.7f, "http://image2.jpg")
        );

        when(movieRepository.findAll()).thenReturn(mockMovieList);
        when(modelMapper.map(mockMovieList, new TypeToken<List<MovieOutDto>>() {}.getType())).thenReturn(modelMapperOut);

        List<MovieOutDto> actualMovieList = movieService.findAll("");
        assertEquals(2, actualMovieList.size());
        assertEquals("Catch Me If You Can", actualMovieList.get(0).getTitle());
        assertEquals("The Matrix", actualMovieList.get(1).getTitle());

        verify(movieRepository, times(1)).findAll();
        verify(movieRepository, times(0)).findByGenre("");
    }

    @Test
    public void testFindAllByGenre() {
        List<Movie> mockMovieList = List.of(
                createMockMovie(1, "Catch Me If You Can", "Action"),
                createMockMovie(2, "Interstellar", "Science Fiction")
        );
        List<MovieOutDto> mockModelMapperOut = List.of(
                new MovieOutDto(1L, "Catch Me If You Can", "A mind-bending thriller", "Action", 8.8f, "http://image1.jpg"),
                new MovieOutDto(2L, "Interstellar", "Space exploration", "Science Fiction", 8.6f, "http://image2.jpg")
        );

        when(movieRepository.findByGenre("Action")).thenReturn(mockMovieList);
        when(modelMapper.map(mockMovieList, new TypeToken<List<MovieOutDto>>() {}.getType())).thenReturn(mockModelMapperOut);

        List<MovieOutDto> actualMovieList = movieService.findAll("Action");
        assertEquals(2, actualMovieList.size());
        assertEquals("Catch Me If You Can", actualMovieList.get(0).getTitle());
        assertEquals("Interstellar", actualMovieList.get(1).getTitle());

        verify(movieRepository, times(0)).findAll();
        verify(movieRepository, times(1)).findByGenre("Action");
    }

    private Movie createMockMovie(long id, String title, String genre) {
        Movie movie = new Movie();
        movie.setId(id);
        movie.setTitle(title);
        movie.setSynopsis("Synopsis for " + title);
        movie.setGenre(genre);
        movie.setReleaseDate(LocalDate.of(2003, 01, 24));
        movie.setDuration(141);
        movie.setAverageRating(8.1f);
        movie.setImageUrl("http://image.jpg");
        return movie;
    }
}
