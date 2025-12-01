package dasturlash.uz.you_tube.dto.profile;

import com.fasterxml.jackson.annotation.JsonInclude;
import dasturlash.uz.you_tube.dto.AttachDTO;
import dasturlash.uz.you_tube.enums.ProfileRole;
import dasturlash.uz.you_tube.enums.ProfileStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProfileDTO {
    private Integer id;
    private String name;
    private String surname;
    private String email;
    private String password;
    private List<ProfileRole> roleList;
    private AttachDTO photo;
    private ProfileStatus status;
    private String jwt;
}