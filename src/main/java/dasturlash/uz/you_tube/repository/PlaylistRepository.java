package dasturlash.uz.you_tube.repository;

import dasturlash.uz.you_tube.entity.PlaylistEntity;
import dasturlash.uz.you_tube.mapper.PlaylistDetailMapper;
import dasturlash.uz.you_tube.mapper.PlaylistInfoMapper;
import dasturlash.uz.you_tube.mapper.PlaylistShortMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlaylistRepository extends CrudRepository<PlaylistEntity, String> {

    @Query("select pl.id as id, pl.name as name, c.videoCount as videoCount, " +
            "pl.createdDate as createdDate, pl.channelId as channelId, c.name as channelName " +
            "from PlaylistEntity pl " +
            "inner join pl.channel c " +
            "where pl.visible = true and pl.status = 'PUBLIC' and pl.channelId = ?1 " +
            "order by pl.orderNum desc")
    List<PlaylistShortMapper> findByChannelId(String channelId);
    
    Optional<PlaylistEntity> findByIdAndProfileId(String id, Integer profileId);
    
    @Query("SELECT p FROM PlaylistEntity p WHERE p.channelId = :channelId ORDER BY p.orderNum DESC")
    List<PlaylistEntity> findByChannelIdOrderByOrderNumDesc(@Param("channelId") String channelId);

    Optional<PlaylistEntity> findByIdAndVisibleTrue(String playlistId);

    @Query("select pl.id as id, pl.name as name, pl.description as description, pl.status as status, " +
            "pl.orderNum as orderNum, c.id as channelId, c.name as channelName, c.photoId as channelPhotoId, " +
            "p.id as profileId, p.name as profileName, p.surname as profileSurname, p.photoId as profilePhotoId " +
            "from PlaylistEntity pl " +
            "inner join pl.channel c " +
            "inner join pl.profile p " +
            "where pl.visible = true " +
            "order by pl.createdDate desc")
    Page<PlaylistInfoMapper> findAllByOrderByCreatedDateDesc(Pageable pageable);

    @Query("select pl.id as id, pl.name as name, pl.description as description, pl.status as status, " +
            "pl.orderNum as orderNum, c.id as channelId, c.name as channelName, c.photoId as channelPhotoId, " +
            "p.id as profileId, p.name as profileName, p.surname as profileSurname, p.photoId as profilePhotoId " +
            "from PlaylistEntity pl " +
            "inner join pl.channel c " +
            "inner join pl.profile p " +
            "where pl.visible = true and pl.profileId = ?1 " +
            "order by pl.createdDate desc")
    List<PlaylistInfoMapper> findByProfileId(Integer profileId);

    @Query("select pl.id as id, pl.name as name, c.videoCount as videoCount, " +
            "pl.createdDate as createdDate, pl.channelId as channelId, c.name as channelName " +
            "from PlaylistEntity pl " +
            "inner join pl.channel c " +
            "where pl.visible = true and pl.profileId = ?1 " +
            "order by pl.createdDate desc")
    List<PlaylistShortMapper> findShortMapperByProfileId(Integer profileId);

    @Query(value = """
        SELECT
            pl.id AS id,
            pl.name AS name,
            c.video_count AS videoCount,
            pl.updated_date AS updatedDate,
            (
                SELECT SUM(v.view_count)
                FROM video v
                WHERE v.channel_id = c.id
            ) AS totalViewCount
        FROM playlist pl
        INNER JOIN channel c ON pl.channel_id = c.id
        WHERE pl.visible = true
          AND pl.id = :playListId
        """, nativeQuery = true)
    Optional<PlaylistDetailMapper> findDetailMapperById(@Param("playListId") String playListId);

}