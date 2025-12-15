package dasturlash.uz.you_tube.dto.playlist_video;

import com.fasterxml.jackson.annotation.JsonInclude;
import dasturlash.uz.you_tube.dto.AttachDTO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlaylistVideoInfoDTO {
    private String playlistVideoId;
    private Integer orderNum;
    private LocalDateTime createdDate;

    private String videoId;
    private String videoTitle;
    private Integer videoDuration;
    private AttachDTO previewAttach;

    private String channelId;
    private String channelName;
}