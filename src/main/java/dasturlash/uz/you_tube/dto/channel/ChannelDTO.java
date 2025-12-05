package dasturlash.uz.you_tube.dto.channel;

import com.fasterxml.jackson.annotation.JsonInclude;
import dasturlash.uz.you_tube.dto.AttachDTO;
import dasturlash.uz.you_tube.enums.ChannelStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChannelDTO {
    private String id;
    private String name;
    private AttachDTO attach;
    private String description;
    private ChannelStatus status;
    private AttachDTO banner;
    private Integer profileId;
    private Boolean visible;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}