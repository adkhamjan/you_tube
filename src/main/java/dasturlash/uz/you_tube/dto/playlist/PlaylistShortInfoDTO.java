package dasturlash.uz.you_tube.dto.playlist;

import com.fasterxml.jackson.annotation.JsonInclude;
import dasturlash.uz.you_tube.dto.channel.ChannelDTO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlaylistShortInfoDTO {
    private String id;
    private String name;
    private LocalDateTime createdDate;
    private ChannelDTO channel;
    private Integer videoCount;
    private String videos; // first 2 videos
}