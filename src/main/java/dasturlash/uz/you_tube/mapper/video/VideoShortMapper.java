package dasturlash.uz.you_tube.mapper.video;

import java.time.LocalDateTime;

public interface VideoShortMapper {

    String getId();
    String getTitle();

    String getPreviewAttachId();

    LocalDateTime getPublishedDate();

    String getChannelId();
    String getChannelName();
    String getChannelPhotoId();

    Long getViewCount();
}