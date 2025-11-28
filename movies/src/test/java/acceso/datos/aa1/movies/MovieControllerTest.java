package acceso.datos.aa1.movies;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import acceso.datos.aa1.movies.controller.MovieController;
import acceso.datos.aa1.movies.dto.MovieOutDto;
import acceso.datos.aa1.movies.service.MovieService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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

    @Test
    public void testGetAll() throws Exception {
        List<MovieOutDto> moviesOutDto = List.of(
                new MovieOutDto(1L, "Catch Me If You Can", "A mind-bending thriller", "Action", 8.1f, "http://image1.jpg"),
                new MovieOutDto(2L, "The Matrix", "A hacker discovers reality", "Science Fiction", 8.7f, "http://image2.jpg")
        );

        when(movieService.findAll("")).thenReturn(moviesOutDto);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/movies")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        List<MovieOutDto> moviesListResponse = objectMapper.readValue(jsonResponse, new TypeReference<>(){});

        assertNotNull(moviesListResponse);
        assertEquals(2, moviesListResponse.size());
        assertEquals("Catch Me If You Can", moviesListResponse.get(0).getTitle());
    }

    @Test
    public void testGetAllByGenre() throws Exception {
        List<MovieOutDto> moviesOutDto = List.of(
                new MovieOutDto(1L, "Catch Me If You Can", "A mind-bending thriller", "Action", 8.1f, "http://image1.jpg"),
                new MovieOutDto(2L, "Interstellar", "Space exploration", "Science Fiction", 8.6f, "http://image2.jpg")
        );

        when(movieService.findAll("Action")).thenReturn(moviesOutDto);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/movies")
                        .queryParam("genre", "Action")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        List<MovieOutDto> moviesListResponse = objectMapper.readValue(jsonResponse, new TypeReference<>(){});

        assertNotNull(moviesListResponse);
        assertEquals(2, moviesListResponse.size());
        assertEquals("Catch Me If You Can", moviesListResponse.get(0).getTitle());
    }
}
