package fr.sirine.starter.user;


import fr.sirine.starter.dto.UserDto;
import fr.sirine.starter.mapper.UserMapper;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> {
                    try {
                        return userMapper.toDto(user);
                    } catch (IOException e) {
                        e.printStackTrace();
                        return null;
                    }
                })
                .filter(Objects::nonNull) // Ensure nulls are filtered out
                .collect(Collectors.toList());
    }

    public void updateUser(Integer id, User user){
        User initialUser = userRepository.findById(id).orElse(null);
        if (initialUser!= null) {
            initialUser.setPseudo(user.getPseudo());
            initialUser.setEmail(user.getEmail());
            initialUser.setFirstname(user.getFirstname());
            initialUser.setPassword(user.getPassword());
            initialUser.setLastname(user.getLastname());
            initialUser.setDateOfBirth(user.getDateOfBirth());
            userRepository.save(initialUser);
        }
    }
    public void deleteUser(Integer id) {

        this.userRepository.deleteById(id);
    }

    @Transactional
    public User findById(Integer id) {

        return this.userRepository.findById(id).orElse(null);
    }

    public User findByEmail(String email) {

        return this.userRepository.findByEmail(email).orElse(null);
    }

    public User getUserByPseudo(String pseudo) {

        return this.userRepository.findByPseudo(pseudo).orElse(null);
    }

}
