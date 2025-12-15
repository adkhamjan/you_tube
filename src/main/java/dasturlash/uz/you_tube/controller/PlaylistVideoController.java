package dasturlash.uz.you_tube.controller;

import dasturlash.uz.you_tube.dto.playlist_video.PlaylistVideoDTO;
import dasturlash.uz.you_tube.dto.playlist_video.PlaylistVideoDeleteDTO;
import dasturlash.uz.you_tube.dto.playlist_video.PlaylistVideoInfoDTO;
import dasturlash.uz.you_tube.entity.PlaylistVideoEntity;
import dasturlash.uz.you_tube.service.PlaylistVideoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/playlist-video")
@Tag(name = "Playlist Video", description = "Playlist Video management APIs")
public class PlaylistVideoController {
    @Autowired
    private PlaylistVideoService playlistVideoService;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Add video to playlist")
    public ResponseEntity<PlaylistVideoDTO> create(@Valid @RequestBody PlaylistVideoDTO dto) {
        log.info("Creating playlist video: playlistId={}, videoId={}", dto.getPlaylistId(), dto.getVideoId());
        return ResponseEntity.ok(playlistVideoService.create(dto));
    }

    @PutMapping
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Update playlist video")
    public ResponseEntity<PlaylistVideoEntity> update(@Valid @RequestBody PlaylistVideoDTO dto) {
        log.info("Updating playlist video: playlistId={}, videoId={}", dto.getPlaylistId(), dto.getVideoId());
        return ResponseEntity.ok(playlistVideoService.update(dto));
    }

    @DeleteMapping
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Remove video from playlist")
    public ResponseEntity<Void> delete(@Valid @RequestBody PlaylistVideoDeleteDTO dto) {
        log.info("Deleting playlist video: playlistId={}, videoId={}", dto.getPlaylistId(), dto.getVideoId());
        playlistVideoService.delete(dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{playlistId}")
    @Operation(summary = "Get videos by playlist ID (Published videos only)")
    public ResponseEntity<PageImpl<PlaylistVideoInfoDTO>> getVideosByPlaylistId(@PathVariable String playlistId,
                                                                               @RequestParam(defaultValue = "1") int page,
                                                                               @RequestParam(defaultValue = "10") int size) {
        log.info("Getting videos for playlist: playlistId={}, page={}, size={}", playlistId, page, size);
        return ResponseEntity.ok(playlistVideoService.getVideosByPlaylistId(playlistId,  page - 1, size));
    }
}