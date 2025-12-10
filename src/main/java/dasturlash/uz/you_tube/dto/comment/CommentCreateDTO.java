package dasturlash.uz.you_tube.dto.comment;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentCreateDTO {
    @NotBlank(message = "Content required")
    private String content;
    @NotBlank(message = "ArticleId required")
    private String videoId;

    private Integer replyId;
}
