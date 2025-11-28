package acceso.datos.aa1.movies;

import acceso.datos.aa1.movies.domain.Studio;
import acceso.datos.aa1.movies.dto.StudioOutDto;
import acceso.datos.aa1.movies.repository.StudioRepository;
import acceso.datos.aa1.movies.service.StudioService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StudioServiceTest {

    @InjectMocks
    private StudioService studioService;

    @Mock
    private StudioRepository studioRepository;

    @Mock
    private ModelMapper modelMapper;

    @Test
    public void testFindAll() {
        List<Studio> mockStudioList = List.of(
                new Studio(1L, "Warner Bros", "USA", 1923, "Los Angeles", "photo1.jpg", true, null),
                new Studio(2L, "Universal Pictures", "USA", 1912, "Universal City", "photo2.jpg", true, null)
        );
        List<StudioOutDto> modelMapperOut = List.of(
                new StudioOutDto(1L, "Warner Bros", "USA", true),
                new StudioOutDto(2L, "Universal Pictures", "USA", true)
        );

        when(studioRepository.findAll()).thenReturn(mockStudioList);
        when(modelMapper.map(mockStudioList, new TypeToken<List<StudioOutDto>>() {}.getType())).thenReturn(modelMapperOut);

        List<StudioOutDto> actualStudioList = studioService.findAll(null, null, null);
        assertEquals(2, actualStudioList.size());
        assertEquals("Warner Bros", actualStudioList.getFirst().getName());
        assertEquals("Universal Pictures", actualStudioList.getLast().getName());

        verify(studioRepository, times(1)).findAll();
        verify(studioRepository, times(0)).findByCountry("USA");
    }

    @Test
    public void testFindAllByCountry() {
        List<Studio> mockStudioList = List.of(
                new Studio(1L, "Warner Bros", "USA", 1923, "Los Angeles", "photo1.jpg", true, null),
                new Studio(2L, "Universal Pictures", "USA", 1912, "Universal City", "photo2.jpg", true, null)
        );
        List<StudioOutDto> mockModelMapperOut = List.of(
                new StudioOutDto(1L, "Warner Bros", "USA", true),
                new StudioOutDto(2L, "Universal Pictures", "USA", true)
        );

        when(studioRepository.findByCountry("USA")).thenReturn(mockStudioList);
        when(modelMapper.map(mockStudioList, new TypeToken<List<StudioOutDto>>() {}.getType())).thenReturn(mockModelMapperOut);

        List<StudioOutDto> actualStudioList = studioService.findAll("USA", null, null);
        assertEquals(2, actualStudioList.size());
        assertEquals("Warner Bros", actualStudioList.getFirst().getName());
        assertEquals("Universal Pictures", actualStudioList.getLast().getName());

        verify(studioRepository, times(0)).findAll();
        verify(studioRepository, times(1)).findByCountry("USA");
    }
}
