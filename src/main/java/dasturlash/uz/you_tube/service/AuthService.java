package dasturlash.uz.you_tube.service;

import dasturlash.uz.you_tube.dto.auth.AuthorizationDTO;
import dasturlash.uz.you_tube.dto.auth.JwtDTO;
import dasturlash.uz.you_tube.dto.auth.RegistrationDTO;
import dasturlash.uz.you_tube.dto.profile.ProfileDTO;
import dasturlash.uz.you_tube.entity.ProfileEntity;
import dasturlash.uz.you_tube.enums.ProfileRole;
import dasturlash.uz.you_tube.enums.ProfileStatus;
import dasturlash.uz.you_tube.exp.AppBadRequestException;
import dasturlash.uz.you_tube.exp.ProfileNotFoundException;
import dasturlash.uz.you_tube.repository.ProfileRepository;
import dasturlash.uz.you_tube.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthService {
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private ProfileRoleService profileRoleService;
    @Autowired
    private ProfileService profileService;
    @Autowired
    private EmailSendingService  emailSendingService;

    public String registration(RegistrationDTO dto) {
        Optional<ProfileEntity> optional = profileRepository.findByEmailAndVisibleTrue(dto.getEmail());
        if (optional.isPresent()) { //
            ProfileEntity existsProfile = optional.get();
            if (existsProfile.getStatus().equals(ProfileStatus.NOT_ACTIVE)) {
                profileRoleService.deleteRolesByProfileId(existsProfile.getId());
                profileRepository.deleteById(existsProfile.getId()); // delete
            } else {
                throw new AppBadRequestException("Username already exists");
            }
        }
        // create profile
        ProfileEntity profile = new ProfileEntity();
        profile.setName(dto.getName());
        profile.setSurname(dto.getSurname());
        profile.setEmail(dto.getEmail());
        profile.setPassword(bCryptPasswordEncoder.encode(dto.getPassword()));
        profile.setVisible(true);
        profile.setStatus(ProfileStatus.NOT_ACTIVE);
        profileRepository.save(profile);
        // save roles
        profileRoleService.create(profile.getId(), List.of(ProfileRole.ROLE_USER));
        // send verification code
        new Thread(() -> emailSendingService.sendRegistrationEmailLink(profile.getEmail(), dto.getName())).start();

        return "code jo'natildi mazgi.";
    }

    public ProfileDTO login(AuthorizationDTO dto) {
        Optional<ProfileEntity> profileOptional = profileRepository.findByEmailAndVisibleTrue(dto.getEmail());
        if (profileOptional.isEmpty()) {
            throw new ProfileNotFoundException("Phone or password wrong");
        }
        ProfileEntity entity = profileOptional.get();
        if (!bCryptPasswordEncoder.matches(dto.getPassword(), entity.getPassword())) {
            throw new UsernameNotFoundException("Phone or password wrong");
        }
        if (!entity.getStatus().equals(ProfileStatus.ACTIVE)) {
            throw new UsernameNotFoundException("Phone or password wrong");
        }
        ProfileDTO response = new ProfileDTO();
        response.setId(entity.getId());
        response.setName(entity.getName());
        response.setSurname(entity.getSurname());
        response.setEmail(entity.getEmail());
        response.setRoleList(profileRoleService.getByProfileId(entity.getId()));
        response.setJwt(JwtUtil.encode(entity.getEmail(),response.getRoleList()));
        return response;
    }

    public String verificationByLink(String id) {
        Optional<ProfileEntity> optional = profileService.getProfileById(Integer.parseInt(id));
        if (optional.isPresent()) {
            profileService.setStatusByUsername(ProfileStatus.ACTIVE, optional.get().getEmail());
            return "Verification Success!";
        }
        throw new AppBadRequestException("Wrong sms code");
    }

    public String verificationByEmail(String jwtToken) {
        JwtDTO jwtDTO = JwtUtil.decode(jwtToken);
        Optional<ProfileEntity> optional = profileService.getProfileById(Integer.parseInt(jwtDTO.getUsername()));
        if (optional.isPresent()) {
            profileService.setStatusByUsername(ProfileStatus.ACTIVE, optional.get().getEmail());
            return "Verification Success!";
        }
        throw new AppBadRequestException("Wrong sms code");
    }
}