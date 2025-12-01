package dasturlash.uz.you_tube.controller;

import dasturlash.uz.you_tube.dto.profile.*;
import dasturlash.uz.you_tube.service.ProfileService;
import dasturlash.uz.you_tube.util.SpringSecurityUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/v1/profile")
public class ProfileController {
    @Autowired
    private ProfileService profileService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProfileDTO> create(@RequestBody @Valid CreateProfileDTO dto) {
        return ResponseEntity.ok(profileService.create(dto));
    }
    
    @PutMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody @Valid ChangePasswordDTO dto) {
        Integer profileId = SpringSecurityUtil.getCurrentUserId();
        return ResponseEntity.ok(profileService.changePassword(profileId, dto));
    }
    
    @PutMapping("/update-email")
    public ResponseEntity<String> updateEmail(@RequestBody @Valid UpdateEmailDTO dto) {
        Integer profileId = SpringSecurityUtil.getCurrentUserId();
        return ResponseEntity.ok(profileService.updateEmail(profileId, dto));
    }
    
    @PutMapping("/update-detail")
    public ResponseEntity<String> updateDetail(@RequestBody @Valid UpdateProfileDetailDTO dto) {
        Integer profileId = SpringSecurityUtil.getCurrentUserId();
        return ResponseEntity.ok(profileService.updateDetail(profileId, dto));
    }
    
    @PutMapping("/update-photo")
    public ResponseEntity<String> updatePhoto(@RequestParam("file") MultipartFile file) {
        Integer profileId = SpringSecurityUtil.getCurrentUserId();
        return ResponseEntity.ok(profileService.updatePhoto(profileId, file));
    }
    
    @GetMapping("/detail")
    public ResponseEntity<ProfileDTO> getDetail() {
        Integer profileId = SpringSecurityUtil.getCurrentUserId();
        return ResponseEntity.ok(profileService.getDetail(profileId));
    }
}