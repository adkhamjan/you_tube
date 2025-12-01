package dasturlash.uz.you_tube.dto.profile;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateProfileDetailDTO {
    @NotBlank(message = "name required")
    private String name;
    @NotBlank(message = "surname required")
    private String surname;
}