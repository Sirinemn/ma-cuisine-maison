package fr.sirine.starter.dto;

import fr.sirine.starter.role.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(hidden = true)
public class UserDto {

    private Integer id;

    @NonNull
    @Size(max = 50)
    @Email
    private String email;

    @NonNull
    @Size(max = 20)
    private String pseudo;

    private List<String> roles;



}
