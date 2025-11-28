package acceso.datos.aa1.movies;

import acceso.datos.aa1.movies.domain.Director;
import acceso.datos.aa1.movies.dto.DirectorOutDto;
import acceso.datos.aa1.movies.repository.DirectorRepository;
import acceso.datos.aa1.movies.service.DirectorService;
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
public class DirectorServiceTest {

    @InjectMocks
    private DirectorService directorService;

    @Mock
    private DirectorRepository directorRepository;

    @Mock
    private ModelMapper modelMapper;

    @Test
    public void testFindAll() {
        List<Director> mockDirectorList = List.of(
                new Director(1L, "Christopher Nolan", "British", LocalDate.of(1970, 7, 30), true, 5, "photo1.jpg", null),
                new Director(2L, "Steven Spielberg", "American", LocalDate.of(1946, 12, 18), true, 3, "photo2.jpg", null)
        );
        List<DirectorOutDto> modelMapperOut = List.of(
                new DirectorOutDto(1L, "Christopher Nolan", "British", true),
                new DirectorOutDto(2L, "Steven Spielberg", "American", true)
        );

        when(directorRepository.findAll()).thenReturn(mockDirectorList);
        when(modelMapper.map(mockDirectorList, new TypeToken<List<DirectorOutDto>>() {}.getType())).thenReturn(modelMapperOut);

        List<DirectorOutDto> actualDirectorList = directorService.findAll(null, null, null);
        assertEquals(2, actualDirectorList.size());
        assertEquals("Christopher Nolan", actualDirectorList.getFirst().getName());
        assertEquals("Steven Spielberg", actualDirectorList.getLast().getName());

        verify(directorRepository, times(1)).findAll();
        verify(directorRepository, times(0)).findByNationality("British");
    }

    @Test
    public void testFindAllByNationality() {
        List<Director> mockDirectorList = List.of(
                new Director(1L, "Christopher Nolan", "British", LocalDate.of(1970, 7, 30), true, 5, "photo1.jpg", null),
                new Director(2L, "Steven Spielberg", "American", LocalDate.of(1946, 12, 18), true, 3, "photo2.jpg", null)
        );
        List<DirectorOutDto> mockModelMapperOut = List.of(
                new DirectorOutDto(1L, "Christopher Nolan", "British", true),
                new DirectorOutDto(2L, "Steven Spielberg", "American", true)
        );

        when(directorRepository.findByNationality("British")).thenReturn(mockDirectorList);
        when(modelMapper.map(mockDirectorList, new TypeToken<List<DirectorOutDto>>() {}.getType())).thenReturn(mockModelMapperOut);

        List<DirectorOutDto> actualDirectorList = directorService.findAll("British", null, null);
        assertEquals(2, actualDirectorList.size());
        assertEquals("Christopher Nolan", actualDirectorList.getFirst().getName());
        assertEquals("Steven Spielberg", actualDirectorList.getLast().getName());

        verify(directorRepository, times(0)).findAll();
        verify(directorRepository, times(1)).findByNationality("British");
    }
}
