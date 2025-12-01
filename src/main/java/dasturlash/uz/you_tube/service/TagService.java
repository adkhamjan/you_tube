package dasturlash.uz.you_tube.service;

import dasturlash.uz.you_tube.entity.TagEntity;
import dasturlash.uz.you_tube.dto.TagDTO;
import dasturlash.uz.you_tube.exp.AppBadRequestException;
import dasturlash.uz.you_tube.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TagEntityService {
    @Autowired
    private TagRepository tagRepository;

    public TagDTO create(TagDTO tagDTO) {
        if (tagRepository.existsByNameAndVisibleTrue(tagDTO.getName())) {
            throw new RuntimeException("TagEntity with this name already exists");
        }

        TagEntity tag = new TagEntity();
        tag.setName(tagDTO.getName());

        tagRepository.save(tag);
        return convertToDTO(tag);
    }

    public TagDTO update(Integer id, TagDTO tagDTO) {
        Optional<TagEntity> optional = tagRepository.findByIdAndVisibleTrue(id);

        if (optional.isEmpty()) {
            throw new AppBadRequestException("TagEntity with this id doesn't exist");
        }
        if (tagRepository.existsByNameAndVisibleTrue(tagDTO.getName())) {
            throw new AppBadRequestException("TagEntity with this name already exists");
        }

        TagEntity tag = optional.get();
        tag.setName(tagDTO.getName());

        tagRepository.save(tag);
        return convertToDTO(tag);
    }

    public void deleteTagEntity(Long id) {
        TagEntity tag = tagRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("TagEntity not found with id: " + id));
        tagRepository.delete(tag);
    }

    // Get All TagEntitys (public list)
    public List<TagDTO> getAllTagEntitys() {
        return tagRepository.findByVisibleTrue().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Get All TagEntitys (admin list - includes hidden)
    public List<TagDTO> getAllTagEntitysAdmin() {
        return tagRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Get TagEntity by ID
    public TagDTO getTagEntityById(Long id) {
        TagEntity tag = tagRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("TagEntity not found with id: " + id));
        return convertToDTO(tag);
    }

    private TagDTO convertToDTO(TagEntity tag) {
        TagDTO dto = new TagDTO();
        dto.setId(tag.getId());
        dto.setName(tag.getName());
        dto.setCreatedDate(tag.getCreatedDate());
        dto.setVisible(tag.getVisible());
        return dto;
    }
}