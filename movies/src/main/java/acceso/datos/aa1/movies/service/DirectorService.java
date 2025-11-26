package acceso.datos.aa1.movies.service;

import acceso.datos.aa1.movies.domain.Director;
import acceso.datos.aa1.movies.dto.DirectorDto;
import acceso.datos.aa1.movies.dto.DirectorOutDto;
import acceso.datos.aa1.movies.exception.DirectorNotFoundException;
import acceso.datos.aa1.movies.repository.DirectorRepository;
import acceso.datos.aa1.movies.util.DateUtil;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DirectorService {

    @Autowired
    private DirectorRepository directorRepository;

    @Autowired
    private ModelMapper modelMapper;

    public Director add(Director director) {
        return directorRepository.save(director);
    }

    public void delete(long id) throws DirectorNotFoundException {
        Director director = directorRepository.findById(id)
                .orElseThrow(DirectorNotFoundException::new);
        directorRepository.delete(director);
    }

    public List<DirectorOutDto> findAll(String nationality) {
        List<Director> directors;

        if (nationality != null && !nationality.isEmpty()) {
            directors = directorRepository.findByNationality(nationality);
        } else {
            directors = directorRepository.findAll();
        }

        return modelMapper.map(directors, new TypeToken<List<DirectorOutDto>>() {}.getType());
    }

    public DirectorDto findById(long id) throws DirectorNotFoundException {
        Director director = directorRepository.findById(id)
                .orElseThrow(DirectorNotFoundException::new);

        DirectorDto directorDto = modelMapper.map(director, DirectorDto.class);

        // Campo calculado
        if (director.getBirthDate() != null) {
            directorDto.setAge(DateUtil.calculateAge(director.getBirthDate()));
        }

        return directorDto;
    }

    public Director modify(long id, Director director) throws DirectorNotFoundException {
        Director existingDirector = directorRepository.findById(id)
                .orElseThrow(DirectorNotFoundException::new);

        modelMapper.map(director, existingDirector);
        existingDirector.setId(id);

        return directorRepository.save(existingDirector);
    }
}
