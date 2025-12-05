package dasturlash.uz.you_tube.mapper;

import dasturlash.uz.you_tube.enums.PlaylistStatus;

public interface PlaylistInfoMapper {
    String getId();
    String getName();
    String getDescription();
    PlaylistStatus getStatus();
    Integer getOrderNum();
    String getChannelId();
    String getChannelName();
    String getChannelPhotoId();
    Integer getProfileId();
    String getProfileName();
    String getProfileSurname();
    String getProfilePhotoId();
}