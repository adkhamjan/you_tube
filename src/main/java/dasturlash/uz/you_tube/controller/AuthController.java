package dasturlash.uz.you_tube.controller;

import dasturlash.uz.you_tube.dto.auth.AuthorizationDTO;
import dasturlash.uz.you_tube.dto.auth.RegistrationDTO;
import dasturlash.uz.you_tube.dto.profile.ProfileDTO;
import dasturlash.uz.you_tube.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/registration")
    public ResponseEntity<String> registration(@Valid @RequestBody RegistrationDTO dto) {
        return ResponseEntity.ok(authService.registration(dto));
    }

    @PostMapping("/login")
    public ResponseEntity<ProfileDTO> login(@Valid @RequestBody AuthorizationDTO dto) {
        return ResponseEntity.ok(authService.login(dto));
    }

    @GetMapping("/registration/email/verification")
    public ResponseEntity<String> verificationByLink(@RequestParam("id") String id) {
        return ResponseEntity.ok(authService.verificationByEmail(id));
    }
}