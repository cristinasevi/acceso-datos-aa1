package acceso.datos.aa1.movies.service;

import acceso.datos.aa1.movies.domain.User;
import acceso.datos.aa1.movies.dto.UserDto;
import acceso.datos.aa1.movies.dto.UserOutDto;
import acceso.datos.aa1.movies.exception.UserNotFoundException;
import acceso.datos.aa1.movies.repository.UserRepository;
import acceso.datos.aa1.movies.util.DateUtil;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    public User add(User user) {
        return userRepository.save(user);
    }

    public void delete(long id) throws UserNotFoundException {
        User user = userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);
        userRepository.delete(user);
    }

    public List<UserOutDto> findAll() {
        List<User> users = userRepository.findAll();
        return modelMapper.map(users, new TypeToken<List<UserOutDto>>() {}.getType());
    }

    public UserDto findById(long id) throws UserNotFoundException {
        User user = userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);

        UserDto userDto = modelMapper.map(user, UserDto.class);

        // Campo calculado
        if (user.getBirthDate() != null) {
            userDto.setAge(DateUtil.calculateAge(user.getBirthDate()));
        }

        return userDto;
    }

    public User modify(long id, User user) throws UserNotFoundException {
        User existingUser = userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);

        modelMapper.map(user, existingUser);
        existingUser.setId(id);

        return userRepository.save(existingUser);
    }
}
