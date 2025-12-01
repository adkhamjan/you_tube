package dasturlash.uz.you_tube.repository;

import dasturlash.uz.you_tube.entity.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<TagEntity, Integer> {
    Optional<TagEntity> findByName(String name);
    List<TagEntity> findByVisibleTrue();
    boolean existsByNameAndVisibleTrue(String name);

    Optional<TagEntity> findByIdAndVisibleTrue(Integer id);
}