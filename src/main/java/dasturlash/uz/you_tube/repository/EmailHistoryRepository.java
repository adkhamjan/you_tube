package dasturlash.uz.you_tube.repository;

import dasturlash.uz.you_tube.entity.EmailHistoryEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EmailHistoryRepository extends CrudRepository<EmailHistoryEntity, String>, PagingAndSortingRepository<EmailHistoryEntity, String> {
    Optional<EmailHistoryEntity> findTopByEmailOrderByCreatedDateDesc(String username);

    List<EmailHistoryEntity> findByEmail(String  username);

    @Query("from EmailHistoryEntity where createdDate between ?1 and ?2")
    List<EmailHistoryEntity> findByDate(LocalDateTime start, LocalDateTime end);
}