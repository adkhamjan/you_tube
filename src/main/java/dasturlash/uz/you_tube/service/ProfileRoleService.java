package dasturlash.uz.you_tube.service;

import dasturlash.uz.you_tube.entity.ProfileRoleEntity;
import dasturlash.uz.you_tube.enums.ProfileRole;
import dasturlash.uz.you_tube.repository.ProfileRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfileRoleService {
    @Autowired
    private ProfileRoleRepository profileRoleRepository;

    public void create(Integer profileId, List<ProfileRole> roleList) {
        for (ProfileRole roleEnum : roleList) {
            ProfileRoleEntity entity = new ProfileRoleEntity();
            entity.setProfileId(profileId);
            entity.setRole(roleEnum);
            profileRoleRepository.save(entity);
        }
    }

    public void update(Integer profileId, List<ProfileRole> roleList) {
        List<ProfileRoleEntity> list = profileRoleRepository.findByProfileId(profileId);
        for (ProfileRoleEntity profileRoleEntity : list) {
            boolean b = false;
            for (ProfileRole role : roleList) {
                if (profileRoleEntity.getRole().equals(role)) {
                    roleList.remove(role);
                    b = true;
                    break;
                }
            }
            if (!b) {
                profileRoleRepository.delete(profileRoleEntity);
            }
        }
        create(profileId, roleList);
    }

    public void deleteRolesByProfileId(Integer profileId) {
        profileRoleRepository.deleteByProfileId(profileId);
    }

    public List<ProfileRole> getByProfileId(Integer profileId) {
        return profileRoleRepository.getRoleListByProfileId(profileId);
    }
}