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
                createMockActor(1, "Leonardo DiCaprio", "American"),
                createMockActor(2, "Tom Hanks", "American")
        );
        List<ActorOutDto> modelMapperOut = List.of(
                new ActorOutDto(1L, "Leonardo DiCaprio", "American", true, "Lead"),
                new ActorOutDto(2L, "Tom Hanks", "American", true, "Lead")
        );

        when(actorRepository.findAll()).thenReturn(mockActorList);
        when(modelMapper.map(mockActorList, new TypeToken<List<ActorOutDto>>() {}.getType())).thenReturn(modelMapperOut);

        List<ActorOutDto> actualActorList = actorService.findAll("");
        assertEquals(2, actualActorList.size());
        assertEquals("Leonardo DiCaprio", actualActorList.get(0).getName());
        assertEquals("Tom Hanks", actualActorList.get(1).getName());

        verify(actorRepository, times(1)).findAll();
        verify(actorRepository, times(0)).findByNationality("");
    }

    @Test
    public void testFindAllByNationality() {
        List<Actor> mockActorList = List.of(
                createMockActor(1, "Leonardo DiCaprio", "American"),
                createMockActor(2, "Tom Hanks", "American")
        );
        List<ActorOutDto> mockModelMapperOut = List.of(
                new ActorOutDto(1L, "Leonardo DiCaprio", "American", true, "Lead"),
                new ActorOutDto(2L, "Tom Hanks", "American", true, "Lead")
        );

        when(actorRepository.findByNationality("American")).thenReturn(mockActorList);
        when(modelMapper.map(mockActorList, new TypeToken<List<ActorOutDto>>() {}.getType())).thenReturn(mockModelMapperOut);

        List<ActorOutDto> actualActorList = actorService.findAll("American");
        assertEquals(2, actualActorList.size());
        assertEquals("Leonardo DiCaprio", actualActorList.get(0).getName());
        assertEquals("Tom Hanks", actualActorList.get(1).getName());

        verify(actorRepository, times(0)).findAll();
        verify(actorRepository, times(1)).findByNationality("American");
    }

    private Actor createMockActor(long id, String name, String nationality) {
        Actor actor = new Actor();
        actor.setId(id);
        actor.setName(name);
        actor.setNationality(nationality);
        actor.setBirthDate(LocalDate.of(1974, 11, 11));
        actor.setActive(true);
        actor.setAwards(1);
        actor.setActorType("Lead");
        return actor;
    }
}
