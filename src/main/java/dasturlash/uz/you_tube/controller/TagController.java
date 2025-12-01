package dasturlash.uz.you_tube.controller;

import dasturlash.uz.you_tube.dto.TagDTO;
import dasturlash.uz.you_tube.service.TagService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tag")
public class TagController {
    @Autowired
    private TagService tagService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping({"", "/"})
    public ResponseEntity<TagDTO> create(@Valid @RequestBody TagDTO dto) {
        return ResponseEntity.ok(tagService.create(dto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<TagDTO> update(@Valid @RequestBody TagDTO dto,
                                         @PathVariable Integer id) {
        return ResponseEntity.ok(tagService.update(id, dto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable Integer id) {
        return ResponseEntity.ok(tagService.delete(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping({"getAll"})
    public ResponseEntity<List<TagDTO>> getAll() {
        return ResponseEntity.ok(tagService.getAllTag());
    }
}