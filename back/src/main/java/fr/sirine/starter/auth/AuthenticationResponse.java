package fr.sirine.starter.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@Schema(hidden = true)
public class AuthenticationResponse {
    private String token;
    private String pseudo;
    private Integer userId;
}
