package dasturlash.uz.you_tube.controller;

import dasturlash.uz.you_tube.dto.CategoryDTO;
import dasturlash.uz.you_tube.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    
    @PostMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryDTO> create(@RequestBody @Valid CategoryDTO dto) {
        return ResponseEntity.ok(categoryService.create(dto));
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Boolean> update(@PathVariable Integer id,
                                             @RequestBody CategoryDTO dto) {
        return ResponseEntity.ok(categoryService.update(id, dto));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Boolean> delete(@PathVariable Integer id) {
        return ResponseEntity.ok(categoryService.delete(id));
    }
    
    @GetMapping("/list")
    public ResponseEntity<List<CategoryDTO>> getList() {
        return ResponseEntity.ok(categoryService.getAll());
    }
}