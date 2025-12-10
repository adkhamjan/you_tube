package dasturlash.uz.you_tube.repository;

import dasturlash.uz.you_tube.entity.CommentEntity;
import dasturlash.uz.you_tube.mapper.CommentMapper;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends CrudRepository<CommentEntity,Integer>, PagingAndSortingRepository<CommentEntity, Integer> {
    Optional<CommentEntity> findByIdAndVisibleTrue(Integer id);

    @Query("select c.id as id, c.createdDate as createdDate, c.content as content, " +
            "c.profileId as  profileId, p.name as profileName, p.surname as profileSurname," +
            "c.likeCount as  likeCount, c.dislikeCount as dislikeCounty, p.photoId as profileImageId " +
            "from CommentEntity c inner join ProfileEntity p on p.id = c.profileId " +
            "where c.videoId = :videoId and c.visible = true order by c.createdDate")
    List<CommentMapper> getByVideoId(@Param("videoId") String videoId);

    @Query("select c.id as id, c.createdDate as createdDate, c.content as content, " +
            "c.videoId as videoId, v.title as videoTitle, v.previewAttachId as previewAttachId, " +
            "a.duration as duration " +
            "from CommentEntity c inner join VideoEntity v on v.id = c.videoId " +
            "inner join AttachEntity a on a.id = v.previewAttachId " +
            "where c.profileId = ?1 and c.visible = true")
    List<CommentMapper> getByProfileId(Integer profileId);

    @Query("select c.id as id, c.createdDate as createdDate, c.content as content, " +
            "c.profileId as  profileId, p.name as profileName, p.surname as profileSurname," +
            "c.likeCount as  likeCount, c.dislikeCount as dislikeCounty, p.photoId as profileImageId " +
            "from CommentEntity c inner join ProfileEntity p on p.id = c.profileId " +
            "where c.visible = true order by c.createdDate")
    PageImpl<CommentMapper> pagination(Pageable pageable);

    @Query("select c.id as id, c.createdDate as createdDate, c.content as content, " +
            "c.profileId as  profileId, p.name as profileName, p.surname as profileSurname," +
            "c.likeCount as  likeCount, c.dislikeCount as dislikeCounty, p.photoId as profileImageId " +
            "from CommentEntity c inner join ProfileEntity p on p.id = c.profileId " +
            "where c.replyId = ?1 and c.visible = true order by c.createdDate")
    List<CommentMapper> getReplyByCommentId(Integer commentId);
}