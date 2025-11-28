package acceso.datos.aa1.movies;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import acceso.datos.aa1.movies.controller.DirectorController;
import acceso.datos.aa1.movies.dto.DirectorOutDto;
import acceso.datos.aa1.movies.service.DirectorService;
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

@WebMvcTest(DirectorController.class)
public class DirectorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DirectorService directorService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetAll() throws Exception {
        List<DirectorOutDto> directorsOutDto = List.of(
                new DirectorOutDto(1L, "Christopher Nolan", "British", true),
                new DirectorOutDto(2L, "Steven Spielberg", "American", true)
        );

        when(directorService.findAll(null, null, null)).thenReturn(directorsOutDto);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/directors")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        List<DirectorOutDto> directorsListResponse = objectMapper.readValue(jsonResponse, new TypeReference<>(){});

        assertNotNull(directorsListResponse);
        assertEquals(2, directorsListResponse.size());
        assertEquals("Christopher Nolan", directorsListResponse.getFirst().getName());
    }

    @Test
    public void testGetAllByNationality() throws Exception {
        List<DirectorOutDto> directorsOutDto = List.of(
                new DirectorOutDto(1L, "Christopher Nolan", "British", true),
                new DirectorOutDto(2L, "Steven Spielberg", "American", true)
        );

        when(directorService.findAll("British", null, null)).thenReturn(directorsOutDto);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/directors")
                        .queryParam("nationality", "British")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        List<DirectorOutDto> directorsListResponse = objectMapper.readValue(jsonResponse, new TypeReference<>(){});

        assertNotNull(directorsListResponse);
        assertEquals(2, directorsListResponse.size());
        assertEquals("Christopher Nolan", directorsListResponse.getFirst().getName());
    }
}
