package dasturlash.uz.you_tube.repository;

import dasturlash.uz.you_tube.entity.CategoryEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CategoryRepository extends CrudRepository<CategoryEntity, Integer> {
    Optional<CategoryEntity> findByName(String name);

    Boolean existsByCategoryKey(String regionKey);

    Boolean existsByCategoryKeyAndIdNot(String regionKey, Integer id);

    @Transactional
    @Modifying
    @Query("update CategoryEntity set visible = false where id = ?1")
    int updateVisibleById(Integer id);

    Optional<CategoryEntity> findByIdAndVisibleTrue(Integer id);
}