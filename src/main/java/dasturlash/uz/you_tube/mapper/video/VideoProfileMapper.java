package dasturlash.uz.you_tube.mapper.video;

import java.time.LocalDateTime;

public interface VideoProfileMapper {
    String getId();
    String getTitle();

    Integer getProfileId();
    String getProfileName();
    String getProfileSurname();

    String getPlaylistId();
    String getPlaylistName();

    String getPreviewAttachId();

    LocalDateTime getPublishedDate();

    String getChannelId();
    String getChannelName();
    String getChannelPhotoId();

    Long getViewCount();
}