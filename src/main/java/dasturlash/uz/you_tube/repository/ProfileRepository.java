package dasturlash.uz.you_tube.repository;

import dasturlash.uz.you_tube.entity.ProfileEntity;
import dasturlash.uz.you_tube.enums.ProfileStatus;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends CrudRepository<ProfileEntity, Integer> {

    Optional<ProfileEntity> findByEmailAndVisibleTrue(String email);

    @Transactional
    @Modifying
    @Query("update ProfileEntity p set p.password = ?2 where p.id = ?1")
    int updatePassword(Integer id, String newPassword);
    
    @Transactional
    @Modifying
    @Query("update ProfileEntity p set p.email = ?2 where p.id = ?1")
    int updateEmail(Integer id, String email);
    
    @Transactional
    @Modifying
    @Query("update ProfileEntity p set p.name = ?2, p.surname = ?3 where p.id = ?1")
    int updateDetail(Integer id, String name, String surname);
    
    @Transactional
    @Modifying
    @Query("update ProfileEntity p set p.photoId = ?2 where p.id = ?1")
    int updatePhoto(Integer id, String photoId);

    Optional<ProfileEntity> findByIdAndVisibleTrue(Integer id);

    Optional<ProfileEntity> findByEmailAndVisibleTrueAndIdNot(String email, Integer id);

    @Transactional
    @Modifying
    @Query("update ProfileEntity set visible = false where id = ?1")
    int updateVisibleById(Integer id);

    @Transactional
    @Modifying
    @Query("update ProfileEntity set status =?1 where email =?2")
    void setStatusByUsername(ProfileStatus status, String emil);

    Optional<ProfileEntity> findByIdAndStatusAndVisibleTrue(Integer id, ProfileStatus status);
}