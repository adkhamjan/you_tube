package dasturlash.uz.you_tube.dto.profile;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateEmailDTO {
    @NotBlank(message = "email required")
    private String email;
}