package fr.sirine.starter.user;


import fr.sirine.starter.dto.UserDto;
import fr.sirine.starter.mapper.UserMapper;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
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

    public void updateUser(String pseudo, String firstname, String lastname, Integer id){
        User initialUser = userRepository.findById(id).orElse(null);
        LocalDateTime now = LocalDateTime.now();
        if (initialUser!= null) {
            initialUser.setPseudo(pseudo);
            initialUser.setFirstname(firstname);
            initialUser.setLastname(lastname);
            initialUser.setLastModifiedDate(now);
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
