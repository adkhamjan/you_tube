package dasturlash.uz.you_tube.repository;

import dasturlash.uz.you_tube.entity.VideoTagEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface VideoTagRepository extends JpaRepository<VideoTagEntity, Integer> {

    List<VideoTagEntity> findByVideoIdAndVisibleTrue(String videoId);

    boolean existsByVideoIdAndTagIdAndVisibleTrue(String videoId, Integer tagId);

    @Transactional
    @Modifying
    @Query("update VideoTagEntity set visible = false where videoId = ?1 and tagId = ?2 ")
    int updateVisible(String videoId, Integer tagId);
}