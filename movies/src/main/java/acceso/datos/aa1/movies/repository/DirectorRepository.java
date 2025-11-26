package acceso.datos.aa1.movies.repository;

import acceso.datos.aa1.movies.domain.Director;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DirectorRepository extends CrudRepository<Director, Long> {

    List<Director> findAll();

    List<Director> findByNationality(String nationality);

    List<Director> findByActive(Boolean active);
}
