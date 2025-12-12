package dasturlash.uz.you_tube.dto.video;

import dasturlash.uz.you_tube.enums.VideoTypeEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VideoUpdateDTO {
    @NotBlank(message = "title required")
    private String title;
    @NotNull(message = "categoryId required")
    private Integer categoryId;
    @NotBlank(message = "description required")
    private String description;
    @NotNull(message = "type required")
    private VideoTypeEnum type;
}
