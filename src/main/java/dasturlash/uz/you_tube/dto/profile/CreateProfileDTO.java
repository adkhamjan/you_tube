package dasturlash.uz.you_tube.dto.profile;

import dasturlash.uz.you_tube.enums.ProfileRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreateProfileDTO {
    @NotBlank(message = "name required")
    private String name;
    @NotBlank(message = "surname required")
    private String surname;
    @NotBlank(message = "email required")
    private String email;
    @NotBlank(message = "password required")
    private String password;
    @NotEmpty(message = "Role bo‘sh bo‘lmasligi kerak")
    private List<ProfileRole> roles;

}