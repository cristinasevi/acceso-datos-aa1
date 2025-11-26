package acceso.datos.aa1.movies.service;

import acceso.datos.aa1.movies.domain.Studio;
import acceso.datos.aa1.movies.dto.StudioDto;
import acceso.datos.aa1.movies.dto.StudioOutDto;
import acceso.datos.aa1.movies.exception.StudioNotFoundException;
import acceso.datos.aa1.movies.repository.StudioRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudioService {

    @Autowired
    private StudioRepository studioRepository;

    @Autowired
    private ModelMapper modelMapper;

    public Studio add(Studio studio) {
        return studioRepository.save(studio);
    }

    public void delete(long id) throws StudioNotFoundException {
        Studio studio = studioRepository.findById(id)
                .orElseThrow(StudioNotFoundException::new);
        studioRepository.delete(studio);
    }

    public List<StudioOutDto> findAll(String country) {
        List<Studio> studios;

        if (country != null && !country.isEmpty()) {
            studios = studioRepository.findByCountry(country);
        } else {
            studios = studioRepository.findAll();
        }

        return modelMapper.map(studios, new TypeToken<List<StudioOutDto>>() {}.getType());
    }

    public StudioDto findById(long id) throws StudioNotFoundException {
        Studio studio = studioRepository.findById(id)
                .orElseThrow(StudioNotFoundException::new);

        return modelMapper.map(studio, StudioDto.class);
    }

    public Studio modify(long id, Studio studio) throws StudioNotFoundException {
        Studio existingStudio = studioRepository.findById(id)
                .orElseThrow(StudioNotFoundException::new);

        modelMapper.map(studio, existingStudio);
        existingStudio.setId(id);

        return studioRepository.save(existingStudio);
    }
}
