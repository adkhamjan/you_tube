package dasturlash.uz.you_tube.dto.video;

import dasturlash.uz.you_tube.dto.AttachDTO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class VideoFullInfoDTO {
    private String id;
    private String title;
    private String description;

    private AttachDTO previewAttach;
    private AttachDTO attach;

    private Integer categoryId;
    private String categoryName;

    private String tagList;

    private LocalDateTime publishedDate;

    private String channelId;
    private String channelName;
    private AttachDTO channelAttach;

    private Long viewCount;
    private Long sharedCount;

    private Long likeCount;
    private Long dislikeCount;
    private Boolean isUserLiked;
    private Boolean isUserDisliked;

    private Integer duration; // seconds
}
