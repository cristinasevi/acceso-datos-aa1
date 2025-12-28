package acceso.datos.aa1.movies;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import acceso.datos.aa1.movies.controller.MovieController;
import acceso.datos.aa1.movies.domain.Movie;
import acceso.datos.aa1.movies.dto.MovieDto;
import acceso.datos.aa1.movies.dto.MovieOutDto;
import acceso.datos.aa1.movies.exception.MovieNotFoundException;
import acceso.datos.aa1.movies.service.MovieService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MovieController.class)
public class MovieControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MovieService movieService;

    @Autowired
    private ObjectMapper objectMapper;

    // GET /movies - 200 OK
    @Test
    public void testGetAll200() throws Exception {
        List<MovieOutDto> moviesOutDto = List.of(
                new MovieOutDto(1L, "Catch Me If You Can", "Synopsis", "Action",
                        LocalDate.of(2003, 1, 24), 141, 8.1f, "http://image1.jpg"),
                new MovieOutDto(2L, "The Matrix", "Synopsis", "Science Fiction",
                        LocalDate.of(1999, 3, 31), 136, 8.7f, "http://image2.jpg")
        );

        when(movieService.findAll(null, null, null, null)).thenReturn(moviesOutDto);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/movies")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        List<MovieOutDto> moviesListResponse = objectMapper.readValue(jsonResponse, new TypeReference<>(){});

        assertNotNull(moviesListResponse);
        assertEquals(2, moviesListResponse.size());
    }

    // GET /movies/{id} - 200 OK
    @Test
    public void testGetById200() throws Exception {
        MovieDto movieDto = new MovieDto(1L, "Catch Me If You Can", "Synopsis",
                LocalDate.of(2003, 1, 24), 141, "Action", 8.1f, "http://image.jpg",
                1L, "Steven Spielberg", "Warner Bros");

        when(movieService.findById(1L)).thenReturn(movieDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/movies/1")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }

    // GET /movies/{id} - 404 NOT FOUND
    @Test
    public void testGetById404() throws Exception {
        when(movieService.findById(999L)).thenThrow(new MovieNotFoundException());

        mockMvc.perform(MockMvcRequestBuilders.get("/movies/999")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound());
    }

    // POST /movies - 201 CREATED
    @Test
    public void testAddMovie201() throws Exception {
        Movie newMovie = new Movie();
        newMovie.setTitle("New Movie");
        newMovie.setSynopsis("Synopsis");
        newMovie.setReleaseDate(LocalDate.of(2025, 12, 1));
        newMovie.setDuration(120);
        newMovie.setGenre("Action");

        Movie savedMovie = new Movie();
        savedMovie.setId(3L);
        savedMovie.setTitle("New Movie");

        when(movieService.add(any(Movie.class))).thenReturn(savedMovie);

        String movieJson = objectMapper.writeValueAsString(newMovie);

        mockMvc.perform(MockMvcRequestBuilders.post("/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(movieJson))
                .andExpect(status().isCreated());
    }

    // POST /movies - 400 BAD REQUEST
    @Test
    public void testAddMovie400() throws Exception {
        String invalidMovieJson = "{ \"synopsis\": \"Missing title\" }"; // Falta "title"

        mockMvc.perform(MockMvcRequestBuilders.post("/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidMovieJson))
                .andExpect(status().isBadRequest());
    }

    // PUT /movies/{id} - 200 OK
    @Test
    public void testModifyMovie200() throws Exception {
        Movie updatedMovie = new Movie();
        updatedMovie.setId(1L);
        updatedMovie.setTitle("Updated Movie");

        when(movieService.modify(anyLong(), any(Movie.class))).thenReturn(updatedMovie);

        String movieJson = objectMapper.writeValueAsString(updatedMovie);

        mockMvc.perform(MockMvcRequestBuilders.put("/movies/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(movieJson))
                .andExpect(status().isOk());
    }

    // PUT /movies/{id} - 404 NOT FOUND
    @Test
    public void testModifyMovie404() throws Exception {
        Movie updatedMovie = new Movie();
        updatedMovie.setTitle("NonExistent Movie");

        when(movieService.modify(anyLong(), any(Movie.class))).thenThrow(new MovieNotFoundException());

        String movieJson = objectMapper.writeValueAsString(updatedMovie);

        mockMvc.perform(MockMvcRequestBuilders.put("/movies/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(movieJson))
                .andExpect(status().isNotFound());
    }

    // DELETE /movies/{id} - 204 NO CONTENT
    @Test
    public void testDeleteMovie204() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/movies/1"))
                .andExpect(status().isNoContent());
    }

    // DELETE /movies/{id} - 404 NOT FOUND
    @Test
    public void testDeleteMovie404() throws Exception {
        org.mockito.Mockito.doThrow(new MovieNotFoundException()).when(movieService).delete(999L);

        mockMvc.perform(MockMvcRequestBuilders.delete("/movies/999"))
                .andExpect(status().isNotFound());
    }
}
