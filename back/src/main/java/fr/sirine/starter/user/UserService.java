package fr.sirine.starter.user;


import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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
