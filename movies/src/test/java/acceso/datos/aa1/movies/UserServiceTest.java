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
                new User(1L, "csevi", "Cristina", "Serrano", "csevi@gmail.com", "password123", LocalDate.now(), true, LocalDate.of(2000, 1, 1), true, null),
                new User(2L, "mdiaz", "Marta", "Díaz", "mdiaz@gmail.com", "password456", LocalDate.now(), false, LocalDate.of(1995, 5, 15), true, null)
        );
        List<UserOutDto> modelMapperOut = List.of(
                new UserOutDto(1L, "csevi", "Cristina", "Serrano", "csevi@gmail.com", true),
                new UserOutDto(2L, "mdiaz", "Marta", "Díaz", "mdiaz@gmail.com", false)
        );

        when(userRepository.findAll()).thenReturn(mockUserList);
        when(modelMapper.map(mockUserList, new TypeToken<List<UserOutDto>>() {}.getType())).thenReturn(modelMapperOut);

        List<UserOutDto> actualUserList = userService.findAll(null, null, null);
        assertEquals(2, actualUserList.size());
        assertEquals("csevi", actualUserList.getFirst().getUsername());
        assertEquals("mdiaz", actualUserList.getLast().getUsername());

        verify(userRepository, times(1)).findAll();
        verify(userRepository, times(0)).findByPremium(true);
    }

    @Test
    public void testFindAllByPremium() {
        List<User> mockUserList = List.of(
                new User(1L, "csevi", "Cristina", "Serrano", "csevi@gmail.com", "password123", LocalDate.now(), true, LocalDate.of(2000, 1, 1), true, null),
                new User(2L, "mdiaz", "Marta", "Díaz", "mdiaz@gmail.com", "password789", LocalDate.now(), true, LocalDate.of(1990, 3, 20), true, null)
        );
        List<UserOutDto> mockModelMapperOut = List.of(
                new UserOutDto(1L, "csevi", "Cristina", "Serrano", "csevi@gmail.com", true),
                new UserOutDto(2L, "mdiaz", "Marta", "Díaz", "mdiaz@gmail.com", true)
        );

        when(userRepository.findByPremium(true)).thenReturn(mockUserList);
        when(modelMapper.map(mockUserList, new TypeToken<List<UserOutDto>>() {}.getType())).thenReturn(mockModelMapperOut);

        List<UserOutDto> actualUserList = userService.findAll(true, null, null);
        assertEquals(2, actualUserList.size());
        assertEquals("csevi", actualUserList.getFirst().getUsername());
        assertEquals("mdiaz", actualUserList.getLast().getUsername());

        verify(userRepository, times(0)).findAll();
        verify(userRepository, times(1)).findByPremium(true);
    }
}
