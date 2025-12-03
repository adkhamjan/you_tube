package dasturlash.uz.you_tube.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthorizationDTO {
    @NotBlank(message = "username required")
    private String email;
    @NotBlank(message = "pswd required")
    private String password;
}