package dasturlash.uz.you_tube.service;

import dasturlash.uz.you_tube.config.CustomUserDetails;
import dasturlash.uz.you_tube.entity.ProfileEntity;
import dasturlash.uz.you_tube.enums.ProfileRole;
import dasturlash.uz.you_tube.repository.ProfileRepository;
import dasturlash.uz.you_tube.repository.ProfileRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    ProfileRoleRepository profileRoleRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // username = login or phone or email
        Optional<ProfileEntity> optional = profileRepository.findByEmailAndVisibleTrue(username);
        if (optional.isEmpty()) {
            throw new UsernameNotFoundException(username);
        }
        ProfileEntity profile = optional.get();
        List<ProfileRole> roleList = profileRoleRepository.getRoleListByProfileId(profile.getId());
        return new CustomUserDetails(profile.getId(),
                profile.getName(),
                profile.getSurname(),
                profile.getEmail(),
                profile.getPassword(),
                roleList,
                profile.getStatus());
    }
}