package acceso.datos.aa1.movies.controller;

import acceso.datos.aa1.movies.domain.Movie;
import acceso.datos.aa1.movies.dto.MovieDto;
import acceso.datos.aa1.movies.dto.MovieOutDto;
import acceso.datos.aa1.movies.exception.ErrorResponse;
import acceso.datos.aa1.movies.exception.MovieNotFoundException;
import acceso.datos.aa1.movies.service.MovieService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class MovieController {

    @Autowired
    private MovieService movieService;

    @GetMapping("/movies")
    public ResponseEntity<List<MovieOutDto>> getAll(@RequestParam(value = "genre", defaultValue = "") String genre) {
        List<MovieOutDto> movies = movieService.findAll(genre);
        return ResponseEntity.ok(movies);
    }

    @GetMapping("/movies/{id}")
    public ResponseEntity<MovieDto> get(@PathVariable long id) throws MovieNotFoundException {
        MovieDto movieDto = movieService.findById(id);
        return ResponseEntity.ok(movieDto);
    }

    @PostMapping("/movies")
    public ResponseEntity<Movie> addMovie(@Valid @RequestBody Movie movie) {
        Movie newMovie = movieService.add(movie);
        return new ResponseEntity<>(newMovie, HttpStatus.CREATED);
    }

    @PutMapping("/movies/{id}")
    public ResponseEntity<Movie> modifyMovie(@PathVariable long id, @RequestBody Movie movie) throws MovieNotFoundException {
        Movie updatedMovie = movieService.modify(id, movie);
        return ResponseEntity.ok(updatedMovie);
    }

    @DeleteMapping("/movies/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable long id) throws MovieNotFoundException {
        movieService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(MovieNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleException(MovieNotFoundException mnfe) {
        ErrorResponse errorResponse = ErrorResponse.notFound("The movie does not exist");
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleException(MethodArgumentNotValidException manve) {
        Map<String, String> errors = new HashMap<>();
        manve.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.put(fieldName, message);
        });

        ErrorResponse errorResponse = ErrorResponse.validationError(errors);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
