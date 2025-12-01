package dasturlash.uz.you_tube.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AttachDTO {
    private String id;
    private String originName;
    private Long size;
    private String type;
    private String path;
    private String url;
    private Long duration;
    private LocalDateTime createdDate;
}