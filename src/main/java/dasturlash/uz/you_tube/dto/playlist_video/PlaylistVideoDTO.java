package dasturlash.uz.you_tube.dto.playlist_video;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlaylistVideoDTO {
    private String id;

    @NotBlank(message = "Playlist ID is required")
    private String playlistId;
    
    @NotBlank(message = "Video ID is required")
    private String videoId;
    
    @NotNull(message = "Order number is required")
    private Integer orderNum;
}