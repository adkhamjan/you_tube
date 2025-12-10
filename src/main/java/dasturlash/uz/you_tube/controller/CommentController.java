package dasturlash.uz.you_tube.controller;

import dasturlash.uz.you_tube.dto.comment.CommentCreateDTO;
import dasturlash.uz.you_tube.dto.comment.CommentProfileDTO;
import dasturlash.uz.you_tube.dto.comment.CommentVideoDTO;
import dasturlash.uz.you_tube.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping({"", "/"})
    public ResponseEntity<CommentProfileDTO> create(@Valid @RequestBody CommentCreateDTO dto) {
        return ResponseEntity.ok(commentService.create(dto));
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/{id}")
    public ResponseEntity<CommentProfileDTO> update(@PathVariable Integer id,
                                                    @Valid @RequestBody CommentCreateDTO dto) {
        return ResponseEntity.ok(commentService.update(id, dto));
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @DeleteMapping("{id}")
    public ResponseEntity<Boolean> delete(@PathVariable Integer id) {
        return ResponseEntity.ok(commentService.delete(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/pagination")
    public ResponseEntity<PageImpl<CommentProfileDTO>> pagination(@RequestParam(value = "page", defaultValue = "1") int page,
                                                                  @RequestParam(value = "size", defaultValue = "10") int size) {
        return ResponseEntity.ok(commentService.pagination(page - 1,size));
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/profile/{id}")
    public ResponseEntity<List<CommentVideoDTO>> getByProfileId(@PathVariable Integer id) {
        return ResponseEntity.ok(commentService.getByProfileId(id));
    }

    @GetMapping("/owner")
    public ResponseEntity<List<CommentVideoDTO>> getByOwnerProfileId() {
        return ResponseEntity.ok(commentService.getByOwner());
    }

    @GetMapping("/video/{id}")
    public ResponseEntity<List<CommentProfileDTO>> getByVideoId(@PathVariable String id) {
        return ResponseEntity.ok(commentService.getByVideoId(id));
    }

    @PostMapping("/reply/{id}")
    public ResponseEntity<List<CommentProfileDTO>> getReplyByCommentId(@PathVariable Integer id) {
        return ResponseEntity.ok(commentService.getReplyByCommentId(id));
    }
}