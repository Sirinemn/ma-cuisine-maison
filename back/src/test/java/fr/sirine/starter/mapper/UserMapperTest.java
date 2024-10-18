package fr.sirine.starter.mapper;

import fr.sirine.starter.MonStarter;
import fr.sirine.starter.dto.UserDto;
import fr.sirine.starter.user.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.time.LocalDateTime;


import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = MonStarter.class)
class UserMapperTest {

    @Autowired
    private  UserMapper userMapper;

    @Test
    public void shouldMapUsertoUserDto() throws IOException {
        LocalDateTime rightNow = LocalDateTime.now();
        User initialUser = new User();
        initialUser.setId(1);
        initialUser.setEmail("john@mail.fr");
        initialUser.setEnabled(true);
        initialUser.setAccountLocked(true);
        initialUser.setFirstname("John");
        initialUser.setLastname("Doe");
        initialUser.setPseudo("JohnD");
        initialUser.setCreatedDate(rightNow);

        UserDto userDto = userMapper.toDto(initialUser);

        assertEquals(initialUser.getPseudo(), userDto.getPseudo());
        assertEquals(initialUser.getEmail(), userDto.getEmail());
        assertEquals(initialUser.getId(), userDto.getId());
    }
    @Test
    public void shouldMapUserStotoUser() throws IOException {
        UserDto userDto = new UserDto();
                userDto.setId(1);
                userDto.setEmail("john@mail.fr");
                userDto.setPseudo("JohnD");

        User user = userMapper.toEntity(userDto);

        assertEquals(userDto.getPseudo(), user.getPseudo());
        assertEquals(userDto.getEmail(), user.getEmail());
        assertEquals(userDto.getId(), user.getId());
    }


}