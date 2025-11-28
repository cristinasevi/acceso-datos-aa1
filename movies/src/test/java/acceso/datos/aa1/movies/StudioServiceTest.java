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
                createMockStudio(1, "Warner Bros", "USA"),
                createMockStudio(2, "Universal Pictures", "USA")
        );
        List<StudioOutDto> modelMapperOut = List.of(
                new StudioOutDto(1L, "Warner Bros", "USA", true),
                new StudioOutDto(2L, "Universal Pictures", "USA", true)
        );

        when(studioRepository.findAll()).thenReturn(mockStudioList);
        when(modelMapper.map(mockStudioList, new TypeToken<List<StudioOutDto>>() {}.getType())).thenReturn(modelMapperOut);

        List<StudioOutDto> actualStudioList = studioService.findAll("");
        assertEquals(2, actualStudioList.size());
        assertEquals("Warner Bros", actualStudioList.get(0).getName());
        assertEquals("Universal Pictures", actualStudioList.get(1).getName());

        verify(studioRepository, times(1)).findAll();
        verify(studioRepository, times(0)).findByCountry("");
    }

    @Test
    public void testFindAllByCountry() {
        List<Studio> mockStudioList = List.of(
                createMockStudio(1, "Warner Bros", "USA"),
                createMockStudio(2, "Universal Pictures", "USA")
        );
        List<StudioOutDto> mockModelMapperOut = List.of(
                new StudioOutDto(1L, "Warner Bros", "USA", true),
                new StudioOutDto(2L, "Universal Pictures", "USA", true)
        );

        when(studioRepository.findByCountry("USA")).thenReturn(mockStudioList);
        when(modelMapper.map(mockStudioList, new TypeToken<List<StudioOutDto>>() {}.getType())).thenReturn(mockModelMapperOut);

        List<StudioOutDto> actualStudioList = studioService.findAll("USA");
        assertEquals(2, actualStudioList.size());
        assertEquals("Warner Bros", actualStudioList.get(0).getName());
        assertEquals("Universal Pictures", actualStudioList.get(1).getName());

        verify(studioRepository, times(0)).findAll();
        verify(studioRepository, times(1)).findByCountry("USA");
    }

    private Studio createMockStudio(long id, String name, String country) {
        Studio studio = new Studio();
        studio.setId(id);
        studio.setName(name);
        studio.setCountry(country);
        studio.setFoundationYear(1923);
        studio.setActive(true);
        return studio;
    }
}
