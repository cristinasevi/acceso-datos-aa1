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

    public List<MovieOutDto> findAll(String genre, LocalDate releaseDateFrom, LocalDate releaseDateTo, Float minRating) {
        List<Movie> movies;

        boolean hasGenre = genre != null && !genre.isEmpty();
        boolean hasFrom = releaseDateFrom != null;
        boolean hasTo = releaseDateTo != null;
        boolean hasRating = minRating != null;

        // Rango de fechas completo (from y to)
        boolean hasDateRange = hasFrom && hasTo;

        if (hasGenre && hasRating && hasDateRange) {
            // 3 filtros: genre + rating + rango fechas
            movies = movieRepository.findByGenreAndAverageRatingGreaterThanEqualAndReleaseDateBetween(
                    genre, minRating, releaseDateFrom, releaseDateTo);
        } else if (hasGenre && hasRating && hasFrom) {
            // genre + rating + desde
            movies = movieRepository.findByGenreAndAverageRatingGreaterThanEqualAndReleaseDateGreaterThanEqual(
                    genre, minRating, releaseDateFrom);
        } else if (hasGenre && hasRating && hasTo) {
            // genre + rating + hasta
            movies = movieRepository.findByGenreAndAverageRatingGreaterThanEqualAndReleaseDateLessThanEqual(
                    genre, minRating, releaseDateTo);
        } else if (hasGenre && hasDateRange) {
            // genre + rango fechas
            movies = movieRepository.findByGenreAndReleaseDateBetween(genre, releaseDateFrom, releaseDateTo);
        } else if (hasGenre && hasFrom) {
            // genre + desde
            movies = movieRepository.findByGenreAndReleaseDateGreaterThanEqual(genre, releaseDateFrom);
        } else if (hasGenre && hasTo) {
            // genre + hasta
            movies = movieRepository.findByGenreAndReleaseDateLessThanEqual(genre, releaseDateTo);
        } else if (hasGenre && hasRating) {
            // genre + rating
            movies = movieRepository.findByGenreAndAverageRatingGreaterThanEqual(genre, minRating);
        } else if (hasRating && hasDateRange) {
            // rating + rango fechas
            movies = movieRepository.findByAverageRatingGreaterThanEqualAndReleaseDateBetween(
                    minRating, releaseDateFrom, releaseDateTo);
        } else if (hasRating && hasFrom) {
            // rating + desde
            movies = movieRepository.findByAverageRatingGreaterThanEqualAndReleaseDateGreaterThanEqual(
                    minRating, releaseDateFrom);
        } else if (hasRating && hasTo) {
            // rating + hasta
            movies = movieRepository.findByAverageRatingGreaterThanEqualAndReleaseDateLessThanEqual(
                    minRating, releaseDateTo);
        } else if (hasGenre) {
            // Solo genre
            movies = movieRepository.findByGenre(genre);
        } else if (hasRating) {
            // Solo rating
            movies = movieRepository.findByAverageRatingGreaterThanEqual(minRating);
        } else if (hasDateRange) {
            // Solo rango fechas
            movies = movieRepository.findByReleaseDateBetween(releaseDateFrom, releaseDateTo);
        } else if (hasFrom) {
            // Solo desde
            movies = movieRepository.findByReleaseDateGreaterThanEqual(releaseDateFrom);
        } else if (hasTo) {
            // Solo hasta
            movies = movieRepository.findByReleaseDateLessThanEqual(releaseDateTo);
        } else {
            // Sin filtros
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
