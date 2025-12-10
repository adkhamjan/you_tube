package dasturlash.uz.you_tube.dto.comment;

import com.fasterxml.jackson.annotation.JsonInclude;
import dasturlash.uz.you_tube.dto.AttachDTO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentProfileDTO {
    Integer id;
    LocalDateTime createdDate;
    String content;
    Integer profileId;
    String profileName;
    String profileSurname;
    AttachDTO profileImage;
    Long likeCount;
    Long dislikeCount;
}
