package acceso.datos.aa1.movies;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import acceso.datos.aa1.movies.controller.UserController;
import acceso.datos.aa1.movies.dto.UserOutDto;
import acceso.datos.aa1.movies.service.UserService;
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

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetAll() throws Exception {
        List<UserOutDto> usersOutDto = List.of(
                new UserOutDto(1L, "csevi", "Cristina", "Serrano", "csevi@gmail.com", true),
                new UserOutDto(2L, "mdiaz", "Marta", "Díaz", "mdiaz@gmail.com", false)
        );

        when(userService.findAll(null, null, null)).thenReturn(usersOutDto);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/users")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        List<UserOutDto> usersListResponse = objectMapper.readValue(jsonResponse, new TypeReference<>(){});

        assertNotNull(usersListResponse);
        assertEquals(2, usersListResponse.size());
        assertEquals("csevi", usersListResponse.getFirst().getUsername());
    }

    @Test
    public void testGetAllByPremium() throws Exception {
        List<UserOutDto> usersOutDto = List.of(
                new UserOutDto(1L, "csevi", "Cristina", "Serrano", "csevi@gmail.com", true),
                new UserOutDto(2L, "mdiaz", "Marta", "Díaz", "mdiaz@gmail.com", true)
        );

        when(userService.findAll(true, null, null)).thenReturn(usersOutDto);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/users")
                        .queryParam("premium", "true")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        List<UserOutDto> usersListResponse = objectMapper.readValue(jsonResponse, new TypeReference<>(){});

        assertNotNull(usersListResponse);
        assertEquals(2, usersListResponse.size());
        assertEquals("csevi", usersListResponse.getFirst().getUsername());
    }
}
