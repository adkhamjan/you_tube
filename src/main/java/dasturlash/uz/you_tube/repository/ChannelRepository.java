package dasturlash.uz.you_tube.repository;

import dasturlash.uz.you_tube.entity.ChannelEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ChannelRepository extends CrudRepository<ChannelEntity, UUID> {
    Page<ChannelEntity> findAllByOrderByCreatedDateDesc(Pageable pageable);

    List<ChannelEntity> findByProfileIdAndVisibleTrueOrderByCreatedDateDesc(Integer profileId);

    boolean existsByName(String name);

    Optional<ChannelEntity> findByIdAndVisibleTrue(UUID channelId);
}