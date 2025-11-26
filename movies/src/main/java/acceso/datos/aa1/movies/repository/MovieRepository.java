package acceso.datos.aa1.movies.repository;

import acceso.datos.aa1.movies.domain.Movie;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends CrudRepository<Movie, Long> {

    List<Movie> findAll();

    List<Movie> findByGenre(String genre);

    List<Movie> findByDirectorId(Long directorId);

    List<Movie> findByStudioId(Long studioId);
}
