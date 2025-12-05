package dasturlash.uz.you_tube.mapper;

import dasturlash.uz.you_tube.dto.channel.ChannelDTO;

import java.time.LocalDateTime;
import java.util.UUID;

public interface PlaylistShortMapper {
    String getId();
    String getName();
    LocalDateTime getCreatedDate();
    String getChannelId();
    String getChannelName();
    Integer getVideoCount();
    String videos();
}
