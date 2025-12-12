package dasturlash.uz.you_tube.dto.video;

import dasturlash.uz.you_tube.enums.PlaylistStatus;
import dasturlash.uz.you_tube.enums.VideoTypeEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class VideoCreateDTO {
    @NotNull(message = "previewAttach required")
    private MultipartFile previewAttach;
    @NotBlank(message = "title required")
    private String title;
    @NotNull(message = "categoryId required")
    private Integer categoryId;
    @NotNull(message = "Attach required")
    private MultipartFile attach;
    @NotBlank(message = "description required")
    private String description;
    @NotNull(message = "type required")
    private VideoTypeEnum type;
    @NotNull(message = "channelId required")
    private String channelId;
}
