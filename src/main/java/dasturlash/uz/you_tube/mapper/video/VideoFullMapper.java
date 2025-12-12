package dasturlash.uz.you_tube.mapper.video;

import dasturlash.uz.you_tube.enums.PlaylistStatus;

import java.time.LocalDateTime;

public interface VideoFullMapper {
    String getId();
    String getTitle();
    String getDescription();
    PlaylistStatus getStatus();

    String getPreviewAttachId();
    String getAttachId();

    Integer getCategoryId();
    String getCategoryName();

    String getTagList();

    LocalDateTime getPublishedDate();

    String getChannelId();
    String getChannelName();
    String getChannelAttachId();

    Long getViewCount();
    Long getSharedCount();

    Long getLikeCount();
    Long getDislikeCount();

    Boolean getIsUserLiked();
    Boolean getIsUserDisliked();
}
