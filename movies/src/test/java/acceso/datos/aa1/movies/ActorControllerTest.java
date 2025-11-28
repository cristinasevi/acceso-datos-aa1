package acceso.datos.aa1.movies;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import acceso.datos.aa1.movies.controller.ActorController;
import acceso.datos.aa1.movies.dto.ActorOutDto;
import acceso.datos.aa1.movies.service.ActorService;
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

@WebMvcTest(ActorController.class)
public class ActorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ActorService actorService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetAll() throws Exception {
        List<ActorOutDto> actorsOutDto = List.of(
                new ActorOutDto(1L, "Leonardo DiCaprio", "American", true, "Lead"),
                new ActorOutDto(2L, "Tom Hanks", "American", true, "Lead")
        );

        when(actorService.findAll("")).thenReturn(actorsOutDto);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/actors")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        List<ActorOutDto> actorsListResponse = objectMapper.readValue(jsonResponse, new TypeReference<>(){});

        assertNotNull(actorsListResponse);
        assertEquals(2, actorsListResponse.size());
        assertEquals("Leonardo DiCaprio", actorsListResponse.get(0).getName());
    }

    @Test
    public void testGetAllByNationality() throws Exception {
        List<ActorOutDto> actorsOutDto = List.of(
                new ActorOutDto(1L, "Leonardo DiCaprio", "American", true, "Lead"),
                new ActorOutDto(2L, "Tom Hanks", "American", true, "Lead")
        );

        when(actorService.findAll("American")).thenReturn(actorsOutDto);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/actors")
                        .queryParam("nationality", "American")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        List<ActorOutDto> actorsListResponse = objectMapper.readValue(jsonResponse, new TypeReference<>(){});

        assertNotNull(actorsListResponse);
        assertEquals(2, actorsListResponse.size());
        assertEquals("Leonardo DiCaprio", actorsListResponse.get(0).getName());
    }
}
