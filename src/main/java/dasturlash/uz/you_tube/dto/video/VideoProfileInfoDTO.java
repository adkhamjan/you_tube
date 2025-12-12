package dasturlash.uz.you_tube.dto.video;

import dasturlash.uz.you_tube.dto.AttachDTO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class VideoProfileInfoDTO {
    private String id;
    private String title;

    private Integer profileId;
    private String profileName;
    private String profileSurname;

    private String playlistId;
    private String playlistName;

    private AttachDTO previewAttach;

    private LocalDateTime publishedDate;

    private String channelId;
    private String channelName;
    private AttachDTO channelAttach;


    private Long viewCount;
    private Integer duration;
}
