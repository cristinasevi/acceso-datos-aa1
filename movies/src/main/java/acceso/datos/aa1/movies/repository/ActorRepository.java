package acceso.datos.aa1.movies.repository;

import acceso.datos.aa1.movies.domain.Actor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActorRepository extends CrudRepository<Actor, Long> {

    List<Actor> findAll();

    List<Actor> findByNationality(String nationality);

    List<Actor> findByActive(Boolean active);
}
