package fr.sirine.starter.mapper;

import fr.sirine.starter.dto.UserDto;
import fr.sirine.starter.user.User;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface UserMapper extends EntityMapper<UserDto, User> {


}