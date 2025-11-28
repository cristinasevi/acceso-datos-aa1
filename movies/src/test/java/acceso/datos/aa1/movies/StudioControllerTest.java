package acceso.datos.aa1.movies;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import acceso.datos.aa1.movies.controller.StudioController;
import acceso.datos.aa1.movies.dto.StudioOutDto;
import acceso.datos.aa1.movies.service.StudioService;
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

@WebMvcTest(StudioController.class)
public class StudioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StudioService studioService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetAll() throws Exception {
        List<StudioOutDto> studiosOutDto = List.of(
                new StudioOutDto(1L, "Warner Bros", "USA", true),
                new StudioOutDto(2L, "Universal Pictures", "USA", true)
        );

        when(studioService.findAll(null, null, null)).thenReturn(studiosOutDto);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/studios")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        List<StudioOutDto> studiosListResponse = objectMapper.readValue(jsonResponse, new TypeReference<>(){});

        assertNotNull(studiosListResponse);
        assertEquals(2, studiosListResponse.size());
        assertEquals("Warner Bros", studiosListResponse.getFirst().getName());
    }

    @Test
    public void testGetAllByCountry() throws Exception {
        List<StudioOutDto> studiosOutDto = List.of(
                new StudioOutDto(1L, "Warner Bros", "USA", true),
                new StudioOutDto(2L, "Universal Pictures", "USA", true)
        );

        when(studioService.findAll("USA", null, null)).thenReturn(studiosOutDto);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/studios")
                        .queryParam("country", "USA")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        List<StudioOutDto> studiosListResponse = objectMapper.readValue(jsonResponse, new TypeReference<>(){});

        assertNotNull(studiosListResponse);
        assertEquals(2, studiosListResponse.size());
        assertEquals("Warner Bros", studiosListResponse.getFirst().getName());
    }
}
