package dasturlash.uz.you_tube.dto.profile;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordDTO {
    private String oldPassword;
    private String newPassword;
}