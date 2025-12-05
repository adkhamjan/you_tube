package dasturlash.uz.you_tube.dto.playlist;

import dasturlash.uz.you_tube.enums.PlaylistStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreatePlaylistDTO {
    @NotNull(message = "Channel ID is required")
    private String channelId;
    
    @NotBlank(message = "Name is required")
    @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters")
    private String name;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

    @NotNull(message = "Status is required")
    private PlaylistStatus status;

    @NotNull(message = "OrderNumber is required")
    private Integer orderNum;
}