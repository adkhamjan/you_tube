package dasturlash.uz.you_tube.repository;

import dasturlash.uz.you_tube.entity.VideoEntity;
import dasturlash.uz.you_tube.enums.PlaylistStatus;
import dasturlash.uz.you_tube.mapper.video.VideoFullMapper;
import dasturlash.uz.you_tube.mapper.video.VideoPlaylistMapper;
import dasturlash.uz.you_tube.mapper.video.VideoProfileMapper;
import dasturlash.uz.you_tube.mapper.video.VideoShortMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VideoRepository extends CrudRepository<VideoEntity, String>, PagingAndSortingRepository<VideoEntity, String> {

    @Query("select v.id as id, v.title as title, v.previewAttachId as previewAttachId, v.publishedDate as publishedDate, " +
            "c.id as channelId, c.name as channelName, c.photoId as channelPhotoId, v.viewCount as viewCount " +
            "from VideoEntity v inner join ChannelEntity c on v.channelId = c.id " +
            "where v.visible = true and v.categoryId = ?1 ")
    PageImpl<VideoShortMapper> findByCategoryId(Integer categoryId, Pageable pageable);

    @Query("select v.id as id, v.title as title, v.previewAttachId as previewAttachId, v.publishedDate as publishedDate, " +
            "c.id as channelId, c.name as channelName, c.photoId as channelPhotoId, v.viewCount as viewCount " +
            "from VideoEntity v inner join ChannelEntity c on v.channelId = c.id " +
            "where v.visible = true and upper(v.title) like upper(concat('%', :title, '%'))")
    PageImpl<VideoShortMapper> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    PageImpl<VideoEntity> findByStatus(PlaylistStatus status, Pageable pageable);

    @Query("""
        select v.id as id,
               v.title as title,
               a.id as previewAttachId,
               v.viewCount as viewCount,
               v.publishedDate as publishedDate
        from VideoEntity v
        join v.previewAttach a
        where v.id = ?1 and v.visible = true order by v.createdDate desc
        """)
    PageImpl<VideoPlaylistMapper> findByChannelIdOrderByCreatedDateDesc(String channelId, Pageable pageable);

    Optional<VideoEntity> findByIdAndVisibleTrue(String id);

    @Query("select v.id as id, v.title as title, v.previewAttachId as previewAttachId, v.publishedDate as publishedDate, " +
            "c.id as channelId, c.name as channelName, c.photoId as channelPhotoId, v.viewCount as viewCount " +
            "from VideoEntity v inner join ChannelEntity c on v.channelId = c.id " +
            "inner join VideoTagEntity vt on vt.videoId = v.id " +
            "where v.visible = true and vt.tagId = ?1 ")
    PageImpl<VideoShortMapper> findByTagId(Integer tagId, Pageable pageable);

    @Query("""
        select
            v.id as id,
            v.title as title,
            v.description as description,
            v.status as status,
            v.previewAttachId as previewAttachId,
            v.attachId as attachId,
            v.categoryId as categoryId,
            c.name as categoryName,
            (select string_agg(t.name, ',')
             from VideoTagEntity vt
             join TagEntity t on t.id = vt.tagId
             where vt.videoId = v.id) as tagList,
            v.publishedDate as publishedDate,
            ch.id as channelId,
            ch.name as channelName,
            ch.photoId as channelAttachId,
            v.viewCount as viewCount,
            v.sharedCount as sharedCount,
            v.likeCount as likeCount,
            v.dislikeCount as dislikeCount
        from VideoEntity v
        join CategoryEntity c on c.id = v.categoryId
        join ChannelEntity ch on ch.id = v.channelId
        where v.id = ?1
        and v.visible = true
        """)
    VideoFullMapper findByVideoId(String id);

    @Query("""
        select v.id as id,
               v.title as title,
               p.id as profileId,
               p.name as profileName,
               p.surname as profileSurname,
               v.previewAttachId as previewAttachId,
               v.publishedDate as publishedDate,
               ch.id as channelId,
               ch.name as channelName,
               v.attachId as channelPhotoId,
               v.viewCount as viewCount
           from VideoEntity v
           inner join ChannelEntity ch on ch.id = v.channelId
           inner join ProfileEntity p on p.id = ch.profileId
        where v.id = :videoId
        """)
    PageImpl<VideoProfileMapper> getAll(Pageable pageable);

}