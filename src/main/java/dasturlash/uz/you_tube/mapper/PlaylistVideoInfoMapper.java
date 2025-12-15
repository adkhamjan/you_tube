package dasturlash.uz.you_tube.mapper;

import java.time.LocalDateTime;

public interface PlaylistVideoInfoMapper {
    String getPlaylistVideoId();
    Integer getOrderNum();
    LocalDateTime getCreatedDate();

    String getVideoId();
    String getVideoTitle(); // seconds
    String getPreviewAttachId();
    LocalDateTime getVideoPublishedDate();

    String getChannelId();
    String getChannelName();
}
