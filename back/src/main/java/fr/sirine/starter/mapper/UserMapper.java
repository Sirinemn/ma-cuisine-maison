package fr.sirine.starter.mapper;

import fr.sirine.starter.dto.UserDto;
import fr.sirine.starter.role.Role;
import fr.sirine.starter.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Mapper(componentModel = "spring")
public interface UserMapper extends EntityMapper<UserDto, User> {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "roles", source = "roles", qualifiedByName = "rolesToNames")
    UserDto toDto(User entity) throws IOException;

    @Mapping(target = "roles", ignore = true)
    User toEntity(UserDto dto);

    @Named("rolesToNames")
    default List<String> rolesToNames(List<Role> roles) {
        return roles.stream().map(Role::getName).collect(Collectors.toList());
    }
}