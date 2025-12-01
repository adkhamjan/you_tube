package dasturlash.uz.you_tube.controller;

import dasturlash.uz.you_tube.dto.AttachDTO;
import dasturlash.uz.you_tube.service.AttachService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/v1/attach")
public class AttachController {
    @Autowired
    private AttachService attachService;
    
    @PostMapping("/upload")
    public ResponseEntity<AttachDTO> upload(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(attachService.upload(file));
    }
    
    @GetMapping("/open/{id}")
    public ResponseEntity<Resource> open(@PathVariable String id) {
        return attachService.open(id);
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> download(@PathVariable String id) {
        return attachService.download(id);
    }

    @GetMapping("/pagination")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<AttachDTO>> pagination(@RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(attachService.pagination(page, size));
    }
    
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Boolean> delete(@PathVariable String id) {
        return ResponseEntity.ok(attachService.delete(id));
    }
}