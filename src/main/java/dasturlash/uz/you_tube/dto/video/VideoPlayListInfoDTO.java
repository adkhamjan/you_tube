package dasturlash.uz.you_tube.dto.video;

import dasturlash.uz.you_tube.dto.AttachDTO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class VideoPlayListInfoDTO {
    private String id;
    private String title;

    private AttachDTO previewAttach;

    private Long viewCount;
    private LocalDateTime publishedDate;
    private Integer duration;
}
