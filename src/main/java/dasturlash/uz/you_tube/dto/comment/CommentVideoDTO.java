package dasturlash.uz.you_tube.dto.comment;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentVideoDTO {
    Integer id;
    LocalDateTime createdDate;
    String content;
    String videoId;
    String previewAttachId;
    String videoTitle;
    Long duration;
    Long likeCount;
    Long dislikeCount;
}
