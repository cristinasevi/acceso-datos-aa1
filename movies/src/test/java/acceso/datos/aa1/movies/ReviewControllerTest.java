package acceso.datos.aa1.movies;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import acceso.datos.aa1.movies.controller.ReviewController;
import acceso.datos.aa1.movies.dto.ReviewOutDto;
import acceso.datos.aa1.movies.service.ReviewService;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReviewController.class)
public class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ReviewService reviewService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetAll() throws Exception {
        List<ReviewOutDto> reviewsOutDto = List.of(
                new ReviewOutDto(1L, "Amazing movie!", 10, LocalDate.of(2025, 11, 27), true, false, "csevi", "Catch Me If You Can"),
                new ReviewOutDto(2L, "Great film", 9, LocalDate.of(2025, 11, 28), true, false, "mdiaz", "The Matrix")
        );

        when(reviewService.findAll(null, null, null)).thenReturn(reviewsOutDto);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/reviews")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        List<ReviewOutDto> reviewsListResponse = objectMapper.readValue(jsonResponse, new TypeReference<>(){});

        assertNotNull(reviewsListResponse);
        assertEquals(2, reviewsListResponse.size());
        assertEquals("Amazing movie!", reviewsListResponse.getFirst().getComment());
    }

    @Test
    public void testGetAllByRecommended() throws Exception {
        List<ReviewOutDto> reviewsOutDto = List.of(
                new ReviewOutDto(1L, "Amazing movie!", 10, LocalDate.of(2025, 11, 27), true, false, "csevi", "Catch Me If You Can"),
                new ReviewOutDto(2L, "Great film", 9, LocalDate.of(2025, 11, 28), true, false, "mdiaz", "The Matrix")
        );

        when(reviewService.findAll(null, true, null)).thenReturn(reviewsOutDto);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/reviews")
                        .queryParam("recommended", "true")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        List<ReviewOutDto> reviewsListResponse = objectMapper.readValue(jsonResponse, new TypeReference<>(){});

        assertNotNull(reviewsListResponse);
        assertEquals(2, reviewsListResponse.size());
        assertEquals("Amazing movie!", reviewsListResponse.getFirst().getComment());
    }
}
