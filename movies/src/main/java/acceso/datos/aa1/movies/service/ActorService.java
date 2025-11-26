package acceso.datos.aa1.movies.service;

import acceso.datos.aa1.movies.domain.Actor;
import acceso.datos.aa1.movies.dto.ActorDto;
import acceso.datos.aa1.movies.dto.ActorOutDto;
import acceso.datos.aa1.movies.exception.ActorNotFoundException;
import acceso.datos.aa1.movies.repository.ActorRepository;
import acceso.datos.aa1.movies.util.DateUtil;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActorService {

    @Autowired
    private ActorRepository actorRepository;

    @Autowired
    private ModelMapper modelMapper;

    public Actor add(Actor actor) {
        return actorRepository.save(actor);
    }

    public void delete(long id) throws ActorNotFoundException {
        Actor actor = actorRepository.findById(id)
                .orElseThrow(ActorNotFoundException::new);
        actorRepository.delete(actor);
    }

    public List<ActorOutDto> findAll(String nationality) {
        List<Actor> actors;

        if (nationality != null && !nationality.isEmpty()) {
            actors = actorRepository.findByNationality(nationality);
        } else {
            actors = actorRepository.findAll();
        }

        return modelMapper.map(actors, new TypeToken<List<ActorOutDto>>() {}.getType());
    }

    public ActorDto findById(long id) throws ActorNotFoundException {
        Actor actor = actorRepository.findById(id)
                .orElseThrow(ActorNotFoundException::new);

        ActorDto actorDto = modelMapper.map(actor, ActorDto.class);

        // Campo calculado
        if (actor.getBirthDate() != null) {
            actorDto.setAge(DateUtil.calculateAge(actor.getBirthDate()));
        }

        return actorDto;
    }

    public Actor modify(long id, Actor actor) throws ActorNotFoundException {
        Actor existingActor = actorRepository.findById(id)
                .orElseThrow(ActorNotFoundException::new);

        modelMapper.map(actor, existingActor);
        existingActor.setId(id);

        return actorRepository.save(existingActor);
    }
}
