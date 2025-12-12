package dasturlash.uz.you_tube.controller;

import dasturlash.uz.you_tube.dto.video.*;
import dasturlash.uz.you_tube.enums.PlaylistStatus;
import dasturlash.uz.you_tube.service.VideoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
@RequestMapping("/api/v1/video")
@Tag(name = "Video Controller", description = "Video management APIs")
public class VideoController {

    @Autowired
    private VideoService videoService;

    // 1. Create Video (USER)
    @PostMapping("")
    @Operation(summary = "Create new video", security = @SecurityRequirement(name = "Bearer Authentication"))
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<VideoShortInfoDTO> create(@Valid @RequestBody VideoCreateDTO dto) {
        log.info("Create video: {}", dto);
        return ResponseEntity.ok(videoService.create(dto));
    }

    // 2. Update Video Detail (USER - owner)
    @PutMapping("/{id}")
    @Operation(summary = "Update video details", security = @SecurityRequirement(name = "Bearer Authentication"))
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<VideoShortInfoDTO> update(@PathVariable String id,
                                                     @Valid @RequestBody VideoUpdateDTO dto) {
        log.info("Update video: {} with data: {}", id, dto);
        return ResponseEntity.ok(videoService.update(id, dto));
    }

    // 3. Change Video Status (USER - owner)
    @PutMapping("/status/{id}")
    @Operation(summary = "Change video status", security = @SecurityRequirement(name = "Bearer Authentication"))
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<VideoShortInfoDTO> changeStatus(@PathVariable String id,
                                                           @RequestParam PlaylistStatus status) {
        log.info("Change video status: {} to {}", id, status);
        return ResponseEntity.ok(videoService.changeStatus(id, status));
    }

    // 4. Increase View Count (PUBLIC)
    @PutMapping("/view/{id}")
    @Operation(summary = "Increase video view count")
    public ResponseEntity<Void> increaseViewCount(@PathVariable String id) {
        log.info("Increase view count for video: {}", id);
        videoService.increaseViewCount(id);
        return ResponseEntity.ok().build();
    }

    // 5. Get Videos by Category (PUBLIC - with pagination)
    @GetMapping("/category/{categoryId}")
    @Operation(summary = "Get videos by category")
    public ResponseEntity<PageImpl<VideoShortInfoDTO>> getByCategoryId(
            @PathVariable Integer categoryId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("Get videos by category: {}, page: {}, size: {}", categoryId, page, size);
        return ResponseEntity.ok(videoService.paginationByCategoryId(categoryId, page - 1, size));
    }

    // 6. Search Videos by Title (PUBLIC - with pagination)
    @GetMapping("/search")
    @Operation(summary = "Search videos by title")
    public ResponseEntity<PageImpl<VideoShortInfoDTO>> search(
            @RequestParam String title,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("Search videos by title: {}, page: {}, size: {}", title, page, size);
        return ResponseEntity.ok(videoService.search(title, page - 1, size));
    }

    // 7. Get Videos by Tag (PUBLIC - with pagination)
    @GetMapping("/tag/{tagId}")
    @Operation(summary = "Get videos by tag")
    public ResponseEntity<PageImpl<VideoShortInfoDTO>> getByTagId(
            @PathVariable Integer tagId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("Get videos by tag: {}, page: {}, size: {}", tagId, page, size);
        return ResponseEntity.ok(videoService.paginationByTagId(tagId, page - 1, size));
    }

    // 8. Get Video by ID (PUBLIC/PRIVATE based on status)
    @GetMapping("/{id}")
    @Operation(summary = "Get video by ID")
    public ResponseEntity<VideoFullInfoDTO> getById(@PathVariable String id) {
        log.info("Get video by id: {}", id);
        return ResponseEntity.ok(videoService.getDTO(id));
    }

    // 9. Get All Videos - Admin (ADMIN only)
    @GetMapping("/admin/all")
    @Operation(summary = "Get all videos (Admin)", security = @SecurityRequirement(name = "Bearer Authentication"))
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PageImpl<VideoProfileInfoDTO>> getAllAdmin(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("Admin: Get all videos, page: {}, size: {}", page - 1, size);
        return ResponseEntity.ok(videoService.getAdminPagination(page, size));
    }

    // 10. Get Channel Videos (PUBLIC - with pagination)
    @GetMapping("/channel/{channelId}/videos")
    @Operation(summary = "Get videos by channel")
    public ResponseEntity<PageImpl<VideoPlayListInfoDTO>> getChannelVideos(@PathVariable String channelId,
                                                                          @RequestParam(defaultValue = "1") int page,
                                                                          @RequestParam(defaultValue = "10") int size) {
        log.info("Get videos by channel: {}, page: {}, size: {}", channelId, page, size);
        return ResponseEntity.ok(videoService.channelVideos(channelId, page - 1, size));
    }
}