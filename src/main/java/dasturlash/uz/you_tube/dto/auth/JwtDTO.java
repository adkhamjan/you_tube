package dasturlash.uz.you_tube.dto.auth;

import dasturlash.uz.you_tube.enums.ProfileRole;

import java.util.List;

public class JwtDTO {
    private String username;
    private List<ProfileRole> roles;

    public JwtDTO(String username, List<ProfileRole> roles) {
        this.username = username;
        this.roles = roles;
    }

    public String getUsername() {
        return username;
    }

    public List<ProfileRole> getRoles() {
        return roles;
    }

}
