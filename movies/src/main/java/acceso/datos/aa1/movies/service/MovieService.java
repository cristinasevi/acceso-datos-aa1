package acceso.datos.aa1.movies.service;

import acceso.datos.aa1.movies.domain.Movie;
import acceso.datos.aa1.movies.dto.MovieDto;
import acceso.datos.aa1.movies.dto.MovieOutDto;
import acceso.datos.aa1.movies.exception.MovieNotFoundException;
import acceso.datos.aa1.movies.repository.MovieRepository;
import acceso.datos.aa1.movies.util.DateUtil;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ModelMapper modelMapper;

    public Movie add(Movie movie) {
        return movieRepository.save(movie);
    }

    public void delete(long id) throws MovieNotFoundException {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(MovieNotFoundException::new);
        movieRepository.delete(movie);
    }

    public List<MovieOutDto> findAll(String genre) {
        List<Movie> movies;

        if (genre != null && !genre.isEmpty()) {
            movies = movieRepository.findByGenre(genre);
        } else {
            movies = movieRepository.findAll();
        }

        return modelMapper.map(movies, new TypeToken<List<MovieOutDto>>() {}.getType());
    }

    public MovieDto findById(long id) throws MovieNotFoundException {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(MovieNotFoundException::new);

        MovieDto movieDto = modelMapper.map(movie, MovieDto.class);

        // Campos calculados
        movieDto.setDaysUntilRelease(
                DateUtil.getDaysBetweenDates(LocalDate.now(), movie.getReleaseDate())
        );

        // Relaciones simplificadas
        if (movie.getDirector() != null) {
            movieDto.setDirectorName(movie.getDirector().getName());
        }
        if (movie.getStudio() != null) {
            movieDto.setStudioName(movie.getStudio().getName());
        }

        return movieDto;
    }

    public Movie modify(long id, Movie movie) throws MovieNotFoundException {
        Movie existingMovie = movieRepository.findById(id)
                .orElseThrow(MovieNotFoundException::new);

        modelMapper.map(movie, existingMovie);
        existingMovie.setId(id);

        return movieRepository.save(existingMovie);
    }
}
