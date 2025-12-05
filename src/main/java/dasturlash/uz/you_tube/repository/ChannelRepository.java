package dasturlash.uz.you_tube.repository;

import dasturlash.uz.you_tube.entity.ChannelEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChannelRepository extends CrudRepository<ChannelEntity, String> {
    Page<ChannelEntity> findAllByOrderByCreatedDateDesc(Pageable pageable);

    List<ChannelEntity> findByProfileIdAndVisibleTrueOrderByCreatedDateDesc(Integer profileId);

    boolean existsByName(String name);

    Optional<ChannelEntity> findByIdAndVisibleTrue(String channelId);
}