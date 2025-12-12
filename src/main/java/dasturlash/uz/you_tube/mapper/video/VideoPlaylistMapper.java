package dasturlash.uz.you_tube.mapper.video;

import java.time.LocalDateTime;

public interface VideoPlaylistMapper {
    String getId();
    String getTitle();

    String getPreviewAttachId();

    Long getViewCount();

    LocalDateTime getPublishedDate();
}
