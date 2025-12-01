package dasturlash.uz.you_tube.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrationDTO {
    @NotBlank(message = "Name required")
    private String name;
    @NotBlank(message = "Surname required")
    private String surname;
    @NotBlank(message = "email required")
    private String email;
    @NotBlank(message = "password required")
    private String password;
}