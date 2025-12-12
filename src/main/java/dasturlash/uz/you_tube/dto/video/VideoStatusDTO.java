package dasturlash.uz.you_tube.dto.video;

import dasturlash.uz.you_tube.enums.PlaylistStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VideoStatusDTO {
    @NotNull(message = "status required")
    private PlaylistStatus status;
}
