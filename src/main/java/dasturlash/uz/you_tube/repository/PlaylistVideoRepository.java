package dasturlash.uz.you_tube.repository;

import dasturlash.uz.you_tube.entity.PlaylistVideoEntity;
import dasturlash.uz.you_tube.enums.PlaylistStatus;
import dasturlash.uz.you_tube.mapper.PlaylistVideoInfoMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PlaylistVideoRepository extends JpaRepository<PlaylistVideoEntity, String> {

    Optional<PlaylistVideoEntity> findByPlaylistIdAndVideoId(String playlistId, String videoId);

    boolean existsByPlaylistIdAndVideoId(String playlistId, String videoId);

    long countByPlaylistIdAndVideo_Status(String playlistId, PlaylistStatus video_status);

    @Query("""
        select pv.id as playlistVideoId,
               pv.orderNum as orderNum,
               pv.createdDate as createdDate,
               v.id as videoId,
               v.title as videoTitle,
               v.previewAttachId as previewAttachId,
               v.publishedDate as videoPublishedDate,
               ch.id as channelId,
               ch.name as channelName
        from PlaylistVideoEntity pv
        inner join VideoEntity v on pv.videoId = v.id
        inner join ChannelEntity ch on v.channelId = ch.id
        where pv.playlistId = ?1 and v.status = 'PUBLIC'
        order by pv.orderNum
        """)
    PageImpl<PlaylistVideoInfoMapper> findPublishedVideosByPlaylistId(String playlistId, Pageable pageable);
}