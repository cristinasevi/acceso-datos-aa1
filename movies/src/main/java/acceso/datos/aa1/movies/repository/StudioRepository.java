package acceso.datos.aa1.movies.repository;

import acceso.datos.aa1.movies.domain.Studio;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudioRepository extends CrudRepository<Studio, Long> {

    List<Studio> findAll();

    List<Studio> findByCountry(String country);

    List<Studio> findByActive(Boolean active);
}
