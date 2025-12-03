package dasturlash.uz.you_tube.service;

import dasturlash.uz.you_tube.dto.CategoryDTO;
import dasturlash.uz.you_tube.entity.CategoryEntity;
import dasturlash.uz.you_tube.exp.AppBadRequestException;
import dasturlash.uz.you_tube.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public CategoryDTO create(CategoryDTO dto) {
        // checking ...
        boolean exists = categoryRepository.existsByCategoryKeyAndVisibleTrue(dto.getCategoryKey().toLowerCase());
        if (exists) {
            throw new AppBadRequestException("Category key exists: " + dto.getCategoryKey());
        }

        CategoryEntity entity = new CategoryEntity();
        entity.setName(dto.getName());
        entity.setCategoryKey(dto.getCategoryKey().toLowerCase());

        categoryRepository.save(entity);

        // response
        dto.setId(entity.getId());
        dto.setCreatedDate(entity.getCreatedDate());
        return dto;
    }

    public Boolean update(Integer id, CategoryDTO dto) {
        Optional<CategoryEntity> optional = categoryRepository.findByIdAndVisibleTrue(id);
        if (optional.isEmpty()) {
            throw new AppBadRequestException("Category not found: " + id);
        }
        boolean exists = categoryRepository.existsByCategoryKeyAndIdNot(dto.getCategoryKey().toLowerCase(), id);
        if (exists) {
            throw new AppBadRequestException("Category key exists: " + dto.getCategoryKey());
        }

        CategoryEntity entity =  optional.get();
        entity.setName(dto.getName());
        entity.setCategoryKey(dto.getCategoryKey().toLowerCase());
        categoryRepository.save(entity);
        return true;
    }

    public Boolean delete(Integer id) {
        return categoryRepository.updateVisibleById(id) == 1;
    }

    public List<CategoryDTO> getAll() {
        Iterable<CategoryEntity> iterable = categoryRepository.findAll();
        List<CategoryDTO> dtoList = new LinkedList<>();
        iterable.forEach(entity -> dtoList.add(toDto(entity)));
        return dtoList;
    }

    private CategoryDTO toDto(CategoryEntity entity) {
        CategoryDTO dto = new CategoryDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setCategoryKey(entity.getCategoryKey().toLowerCase());
        dto.setVisible(entity.getVisible());
        dto.setCreatedDate(entity.getCreatedDate());
        return dto;
    }

    public CategoryEntity get(Integer id) {
        return categoryRepository.findById(id).orElseThrow(() -> {
            throw new AppBadRequestException("Item not found");
        });
    }
}