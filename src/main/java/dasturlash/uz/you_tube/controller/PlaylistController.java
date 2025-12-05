package dasturlash.uz.you_tube.controller;

import dasturlash.uz.you_tube.dto.playlist.*;
import dasturlash.uz.you_tube.enums.PlaylistStatus;
import dasturlash.uz.you_tube.service.PlaylistService;
import dasturlash.uz.you_tube.util.SpringSecurityUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/playlist")
public class PlaylistController {
    @Autowired
    private PlaylistService playlistService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping("")
    public ResponseEntity<PlaylistInfoDTO> createPlaylist(@Valid @RequestBody CreatePlaylistDTO dto) {
        return ResponseEntity.ok(playlistService.create(dto));
    }
    
    // 2. Update Playlist (USER and OWNER)
    @PreAuthorize("hasRole('USER')")
    @PutMapping("/{playlistId}")
    public ResponseEntity<PlaylistInfoDTO> updatePlaylist(@PathVariable String playlistId,
                                                          @Valid @RequestBody UpdatePlaylistDTO dto) {
        return ResponseEntity.ok(playlistService.updatePlaylist(playlistId, dto));
    }
    
    // 3. Change Playlist Status (USER and OWNER)
    @PreAuthorize("hasRole('USER')")
    @PutMapping("/{playlistId}/status")
    public ResponseEntity<PlaylistInfoDTO> changePlaylistStatus(@PathVariable String playlistId,
                                                                @RequestParam PlaylistStatus status) {
        return ResponseEntity.ok(playlistService.changePlaylistStatus(playlistId, status));
    }
    
    // 4. Delete Playlist (USER and OWNER, ADMIN)
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @DeleteMapping("/{playlistId}")
    public ResponseEntity<Boolean> deletePlaylist(@PathVariable String playlistId) {
        return ResponseEntity.ok(playlistService.deletePlaylist(playlistId));
    }
    
    // 5. Playlist Pagination (ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/pagination")
    public ResponseEntity<Page<PlaylistInfoDTO>> getPlaylistPagination(@RequestParam(defaultValue = "1") int page,
                                                                       @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(playlistService.getPlaylistPagination(page - 1, size));
    }
    
    // 6. Playlist List By UserId (ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/by-user/{userId}")
    public ResponseEntity<List<PlaylistInfoDTO>> getPlaylistByUserId(@PathVariable Integer userId) {
        return ResponseEntity.ok(playlistService.getPlaylistByUserId(userId));
    }
    
    // 7. Get User Playlist (murojat qilgan user ni)
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/my-playlists")
    public ResponseEntity<List<PlaylistShortInfoDTO>> getUserPlaylist() {
        Integer profileId = SpringSecurityUtil.getCurrentUserId();
        return ResponseEntity.ok(playlistService.getUserPlaylist(profileId));
    }
    
    // 8. Get Channel Play List By ChannelKey (only Public)
    @GetMapping("/channel/{channelId}")
    public ResponseEntity<List<PlaylistShortInfoDTO>> getChannelPlayList(@PathVariable String channelId) {
        return ResponseEntity.ok(playlistService.getChannelPlayList(channelId));
    }
    
    // 9. Get Playlist by id
    @GetMapping("/{playlistId}")
    public ResponseEntity<PlaylistDetailDTO> getPlaylistById(@PathVariable String playlistId) {
        return ResponseEntity.ok(playlistService.getPlaylistById(playlistId));
    }
}