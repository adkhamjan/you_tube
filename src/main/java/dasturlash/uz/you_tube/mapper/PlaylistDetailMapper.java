package dasturlash.uz.you_tube.mapper;

import java.time.LocalDateTime;

public interface PlaylistDetailMapper {
    String getId();
    String getName();
    Integer getVideoCount();
    Long getTotalViewCount();
    LocalDateTime getLastUpdateDate();
}
