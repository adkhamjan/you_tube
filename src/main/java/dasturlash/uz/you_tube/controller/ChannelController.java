package dasturlash.uz.you_tube.controller;

import dasturlash.uz.you_tube.dto.channel.ChannelDTO;
import dasturlash.uz.you_tube.dto.channel.CreateChannelDTO;
import dasturlash.uz.you_tube.dto.channel.UpdateChannelDTO;
import dasturlash.uz.you_tube.enums.ChannelStatus;
import dasturlash.uz.you_tube.service.ChannelService;
import dasturlash.uz.you_tube.util.SpringSecurityUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/channel")
public class ChannelController {
    @Autowired
    private ChannelService channelService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping("")
    public ResponseEntity<ChannelDTO> createChannel(@Valid @RequestBody CreateChannelDTO dto) {
        return ResponseEntity.ok(channelService.create(dto));
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/{channelId}")
    public ResponseEntity<ChannelDTO> updateChannel(@PathVariable UUID channelId,
                                                    @Valid @RequestBody UpdateChannelDTO dto) {
        return ResponseEntity.ok(channelService.updateChannel(channelId, dto));
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/{channelId}/photo")
    public ResponseEntity<ChannelDTO> updateChannelPhoto(@PathVariable UUID channelId,
                                                         @RequestParam("photo") MultipartFile photo) {
        return ResponseEntity.ok(channelService.updateChannelPhoto(channelId, photo));
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/{channelId}/banner")
    public ResponseEntity<ChannelDTO> updateChannelBanner(@PathVariable UUID channelId,
                                                          @RequestParam("banner") MultipartFile banner) {
        return ResponseEntity.ok(channelService.updateChannelBanner(channelId, banner));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/pagination")
    public ResponseEntity<Page<ChannelDTO>> getChannelPagination(@RequestParam(defaultValue = "0") int page,
                                                                 @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(channelService.getChannelPagination(page - 1, size));
    }

    @GetMapping("/{channelId}")
    public ResponseEntity<ChannelDTO> getChannelById(@PathVariable UUID channelId) {
        return ResponseEntity.ok(channelService.getChannelById(channelId));
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PutMapping("/{channelId}/status")
    public ResponseEntity<ChannelDTO> changeChannelStatus(@PathVariable UUID channelId,
                                                          @RequestParam ChannelStatus status) {
        return ResponseEntity.ok(channelService.changeChannelStatus(channelId, status));
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/my-channels")
    public ResponseEntity<List<ChannelDTO>> getUserChannelList() {
        return ResponseEntity.ok(channelService.getUserChannelList(SpringSecurityUtil.getCurrentUserId()));
    }
}