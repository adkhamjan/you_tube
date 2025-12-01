package dasturlash.uz.you_tube.repository;

import dasturlash.uz.you_tube.entity.ProfileRoleEntity;
import dasturlash.uz.you_tube.enums.ProfileRole;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfileRoleRepository extends CrudRepository<ProfileRoleEntity, Integer> {

    List<ProfileRoleEntity> findByProfileId(Integer profileId);

    @Transactional
    @Modifying
    void deleteByProfileId(Integer profileId);

    @Query("select role from ProfileRoleEntity where profileId =?1")
    List<ProfileRole> getRoleListByProfileId(Integer profileId);

}