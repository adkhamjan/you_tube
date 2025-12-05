package dasturlash.uz.you_tube.dto.playlist;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlaylistDetailDTO {
    private String id;
    private String name;
    private Integer videoCount;
    private Long totalViewCount;
    private LocalDateTime lastUpdateDate;
}
