package dasturlash.uz.you_tube.mapper;

import java.time.LocalDateTime;

public interface CommentMapper {
    Integer getId();

    LocalDateTime getCreatedDate();

    String getContent();

    String getVideoId();
    String getPreviewAttachId();
    Long getDuration();
    String getVideoTitle();

    Integer getProfileId();
    String getProfileName();
    String getProfileSurname();
    String getProfileImageId();

    Long getLikeCount();
    Long getDislikeCount();
}