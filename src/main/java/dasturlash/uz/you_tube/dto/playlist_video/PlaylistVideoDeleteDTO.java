package dasturlash.uz.you_tube.dto.playlist_video;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlaylistVideoDeleteDTO {
    
    @NotBlank(message = "Playlist ID is required")
    private String playlistId;
    
    @NotBlank(message = "Video ID is required")
    private String videoId;
}