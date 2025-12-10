package dasturlash.uz.you_tube.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VideoTagDTO {
    private Integer id;

    @NotBlank(message = "videoId is required")
    private String videoId;
    @NotNull(message = "tagId is required")
    private Integer tagId;

    private String tagName;

    private LocalDateTime createdDate;
}
