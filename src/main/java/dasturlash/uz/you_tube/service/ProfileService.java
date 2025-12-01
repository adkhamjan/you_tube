package dasturlash.uz.you_tube.service;

import dasturlash.uz.you_tube.dto.AttachDTO;
import dasturlash.uz.you_tube.dto.profile.*;
import dasturlash.uz.you_tube.entity.ProfileEntity;
import dasturlash.uz.you_tube.enums.ProfileStatus;
import dasturlash.uz.you_tube.exp.AppBadRequestException;
import dasturlash.uz.you_tube.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
public class ProfileService {
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private AttachService attachService;
    @Autowired
    BCryptPasswordEncoder  bCryptPasswordEncoder;
    @Autowired
    ProfileRoleService profileRoleService;

    public ProfileDTO create(CreateProfileDTO profile) {
        // checking
        Optional<ProfileEntity> optional = profileRepository.findByEmailAndVisibleTrue(profile.getEmail());
        if (optional.isPresent()) {
            throw new AppBadRequestException("User exists");
        }
        ProfileEntity entity = new ProfileEntity();
        entity.setName(profile.getName());
        entity.setSurname(profile.getSurname());
        entity.setPassword(bCryptPasswordEncoder.encode(profile.getPassword()));
        entity.setEmail(profile.getEmail());
        entity.setStatus(ProfileStatus.ACTIVE);
        entity.setVisible(Boolean.TRUE);
        profileRepository.save(entity); // save
        // save roles
        profileRoleService.create(entity.getId(), profile.getRoles());

        return toDto(entity);
    }

    public String changePassword(Integer profileId, ChangePasswordDTO dto) {
        Optional<ProfileEntity> optional = profileRepository.findByIdAndVisibleTrue(profileId);
        if (optional.isEmpty()) {
            throw new AppBadRequestException("User not found");
        }
        ProfileEntity entity = optional.get();

        if (!bCryptPasswordEncoder.matches(dto.getOldPassword(), entity.getPassword())) {
            throw new AppBadRequestException("Old password incorrect");
        }

        int result = profileRepository.updatePassword(profileId, bCryptPasswordEncoder.encode(dto.getNewPassword()));
        if (result == 0) {
            throw new AppBadRequestException("Password update failed");
        }

        return "Password changed successfully";
    }

    public String updateEmail(Integer profileId, UpdateEmailDTO dto) {
        if (profileRepository.findByEmailAndVisibleTrue(dto.getEmail()).isPresent()) {
            throw new AppBadRequestException("Email already exists");
        }

        int result = profileRepository.updateEmail(profileId, dto.getEmail());
        if (result == 0) {
            throw new AppBadRequestException("Email update failed");
        }

        return "Email updated successfully. Please verify your email.";
    }

    public String updateDetail(Integer profileId, UpdateProfileDetailDTO dto) {
        int result = profileRepository.updateDetail(profileId, dto.getName(), dto.getSurname());
        if (result == 0) {
            throw new AppBadRequestException("Profile update failed");
        }
        return "Profile updated successfully";
    }

    public String updatePhoto(Integer profileId, MultipartFile file) {
        ProfileEntity profile = get(profileId);

        // Delete old photo
        if (profile.getPhotoId() != null) {
            attachService.delete(profile.getPhotoId());
        }
        AttachDTO attach = attachService.upload(file);

        int result = profileRepository.updatePhoto(profileId, attach.getId());
        if (result == 0) {
            throw new AppBadRequestException("Photo update failed");
        }

        return "Photo updated successfully";
    }

    public ProfileDTO getDetail(Integer profileId) {
        ProfileEntity profile = get(profileId);

        ProfileDTO dto = toDto(profile);

        if (profile.getPhotoId() != null) {
            dto.setPhoto(attachService.openDTO(profile.getPhotoId()));
        }
        return dto;
    }

    public ProfileEntity get(Integer id) {
        Optional<ProfileEntity> optional =  profileRepository.findByIdAndVisibleTrue(id);
        if (optional.isEmpty()) {
            throw new AppBadRequestException("Item not found");
        }
        return optional.get();
    }

    private ProfileDTO toDto(ProfileEntity entity) {
        return null;
    }

    public Optional<ProfileEntity> getProfileById(Integer id) {
        return profileRepository.findByIdAndStatusAndVisibleTrue(id, ProfileStatus.NOT_ACTIVE);
    }

    public void setStatusByUsername(ProfileStatus status, String username) {
        profileRepository.setStatusByUsername(status, username);
    }

    public Integer getIdByUsername(String username) {
        Optional<ProfileEntity> optional =  profileRepository.findByEmailAndVisibleTrue(username);
        if (optional.isEmpty()) {
            throw new AppBadRequestException("Username not found");
        }
        return optional.get().getId();
    }
}