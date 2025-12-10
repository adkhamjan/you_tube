package dasturlash.uz.you_tube.controller;

import dasturlash.uz.you_tube.dto.VideoTagDTO;
import dasturlash.uz.you_tube.service.VideoTagService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/video-tag")
public class VideoTagController {
    @Autowired
    private VideoTagService videoTagService;

    @PostMapping("/add")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<VideoTagDTO> addTag(@RequestBody @Valid VideoTagDTO dto) {
        return ResponseEntity.ok(videoTagService.addTag(dto));
    }

    @DeleteMapping("")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Boolean> deleteTag(@RequestParam("videoId") String videoId,
                                             @RequestParam("tagId") Integer tagId
    ) {
        return ResponseEntity.ok(videoTagService.deleteTag(videoId, tagId));
    }

    @GetMapping("/{videoId}")
    public ResponseEntity<List<VideoTagDTO>> getList(@PathVariable String videoId) {
        return ResponseEntity.ok(videoTagService.getByVideoId(videoId));
    }
}
