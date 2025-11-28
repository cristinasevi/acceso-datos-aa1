package acceso.datos.aa1.movies;

import acceso.datos.aa1.movies.domain.User;
import acceso.datos.aa1.movies.dto.UserOutDto;
import acceso.datos.aa1.movies.repository.UserRepository;
import acceso.datos.aa1.movies.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper modelMapper;

    @Test
    public void testFindAll() {
        List<User> mockUserList = List.of(
                createMockUser(1, "csevi", "Cristina", "Serrano"),
                createMockUser(2, "mdiaz", "Marta", "Díaz")
        );
        List<UserOutDto> modelMapperOut = List.of(
                new UserOutDto(1L, "csevi", "Cristina", "Serrano", "csevi@gmail.com", true),
                new UserOutDto(2L, "mdiaz", "Marta", "Díaz", "mdiaz@gmail.com", false)
        );

        when(userRepository.findAll()).thenReturn(mockUserList);
        when(modelMapper.map(mockUserList, new TypeToken<List<UserOutDto>>() {}.getType())).thenReturn(modelMapperOut);

        List<UserOutDto> actualUserList = userService.findAll();
        assertEquals(2, actualUserList.size());
        assertEquals("csevi", actualUserList.get(0).getUsername());
        assertEquals("mdiaz", actualUserList.get(1).getUsername());

        verify(userRepository, times(1)).findAll();
    }

    private User createMockUser(long id, String username, String name, String surname) {
        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setName(name);
        user.setSurname(surname);
        user.setEmail(username + "@gmail.com");
        user.setPassword("password123");
        user.setRegistrationDate(LocalDate.now());
        user.setPremium(id == 1);
        user.setBirthDate(LocalDate.of(2000, 1, 1));
        return user;
    }
}
