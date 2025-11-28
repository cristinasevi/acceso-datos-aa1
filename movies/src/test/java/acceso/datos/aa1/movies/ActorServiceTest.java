package acceso.datos.aa1.movies;

import acceso.datos.aa1.movies.domain.Actor;
import acceso.datos.aa1.movies.dto.ActorOutDto;
import acceso.datos.aa1.movies.repository.ActorRepository;
import acceso.datos.aa1.movies.service.ActorService;
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
public class ActorServiceTest {

    @InjectMocks
    private ActorService actorService;

    @Mock
    private ActorRepository actorRepository;

    @Mock
    private ModelMapper modelMapper;

    @Test
    public void testFindAll() {
        List<Actor> mockActorList = List.of(
                new Actor(1L, "Leonardo DiCaprio", "American", LocalDate.of(1974, 11, 11), true, 1, "Lead", "photo1.jpg", null),
                new Actor(2L, "Tom Hanks", "American", LocalDate.of(1956, 7, 9), true, 2, "Lead", "photo2.jpg", null)
        );
        List<ActorOutDto> modelMapperOut = List.of(
                new ActorOutDto(1L, "Leonardo DiCaprio", "American", true, "Lead"),
                new ActorOutDto(2L, "Tom Hanks", "American", true, "Lead")
        );

        when(actorRepository.findAll()).thenReturn(mockActorList);
        when(modelMapper.map(mockActorList, new TypeToken<List<ActorOutDto>>() {}.getType())).thenReturn(modelMapperOut);

        List<ActorOutDto> actualActorList = actorService.findAll(null, null, null);
        assertEquals(2, actualActorList.size());
        assertEquals("Leonardo DiCaprio", actualActorList.getFirst().getName());
        assertEquals("Tom Hanks", actualActorList.getLast().getName());

        verify(actorRepository, times(1)).findAll();
        verify(actorRepository, times(0)).findByNationality("American");
    }

    @Test
    public void testFindAllByNationality() {
        List<Actor> mockActorList = List.of(
                new Actor(1L, "Leonardo DiCaprio", "American", LocalDate.of(1974, 11, 11), true, 1, "Lead", "photo1.jpg", null),
                new Actor(2L, "Tom Hanks", "American", LocalDate.of(1956, 7, 9), true, 2, "Lead", "photo2.jpg", null)
        );
        List<ActorOutDto> mockModelMapperOut = List.of(
                new ActorOutDto(1L, "Leonardo DiCaprio", "American", true, "Lead"),
                new ActorOutDto(2L, "Tom Hanks", "American", true, "Lead")
        );

        when(actorRepository.findByNationality("American")).thenReturn(mockActorList);
        when(modelMapper.map(mockActorList, new TypeToken<List<ActorOutDto>>() {}.getType())).thenReturn(mockModelMapperOut);

        List<ActorOutDto> actualActorList = actorService.findAll("American", null, null);
        assertEquals(2, actualActorList.size());
        assertEquals("Leonardo DiCaprio", actualActorList.getFirst().getName());
        assertEquals("Tom Hanks", actualActorList.getLast().getName());

        verify(actorRepository, times(0)).findAll();
        verify(actorRepository, times(1)).findByNationality("American");
    }
}
