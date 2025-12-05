package dasturlash.uz.you_tube.dto.playlist;

import com.fasterxml.jackson.annotation.JsonInclude;
import dasturlash.uz.you_tube.dto.channel.ChannelDTO;
import dasturlash.uz.you_tube.dto.profile.ProfileDTO;
import dasturlash.uz.you_tube.enums.PlaylistStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlaylistInfoDTO {
    private String id;
    private String name;
    private String description;
    private PlaylistStatus status;
    private Integer orderNum;
    private ChannelDTO channel;
    private ProfileDTO profile;
}