package dasturlash.uz.you_tube.service;

import dasturlash.uz.you_tube.dto.VideoTagDTO;
import dasturlash.uz.you_tube.entity.TagEntity;
import dasturlash.uz.you_tube.entity.VideoEntity;
import dasturlash.uz.you_tube.entity.VideoTagEntity;
import dasturlash.uz.you_tube.exp.AppAccessDeniedException;
import dasturlash.uz.you_tube.exp.AppBadRequestException;
import dasturlash.uz.you_tube.repository.VideoTagRepository;
import dasturlash.uz.you_tube.util.SpringSecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class VideoTagService {
    @Autowired
    private VideoTagRepository videoTagRepository;
    @Autowired
    private VideoService videoService;
    @Autowired
    private TagService tagService;

    public VideoTagDTO addTag(VideoTagDTO dto) {
        Integer profileId = SpringSecurityUtil.getCurrentUserId();
        // video check
        VideoEntity videoEntity = videoService.get(dto.getVideoId());
        if (!videoEntity.getChannel().getProfileId().equals(profileId)) {
            throw new AppAccessDeniedException("Not access video");
        }

        // tag check
        TagEntity tag = tagService.getTagById(dto.getTagId());

        // already exists?
        if(videoTagRepository.existsByVideoIdAndTagIdAndVisibleTrue(dto.getVideoId(), dto.getTagId())) {
            throw new AppBadRequestException("Tag already added to video");
        }

        VideoTagEntity entity = new VideoTagEntity();
        entity.setVideoId(dto.getVideoId());
        entity.setTagId(dto.getTagId());
        videoTagRepository.save(entity);

        return toDTO(entity);
    }

    // 2. Delete tag
    public Boolean deleteTag(String videoId, Integer tagId) {
        Integer profileId = SpringSecurityUtil.getCurrentUserId();

        VideoEntity videoEntity = videoService.get(videoId);
        if (!videoEntity.getChannel().getProfileId().equals(profileId)) {
            throw new AppAccessDeniedException("Not access video");
        }

        if (videoTagRepository.updateVisible(videoId, tagId) == 0) {
            throw new AppBadRequestException("Tag not assigned to video");
        }
        return true;
    }

    // 3. Get list by videoId
    public List<VideoTagDTO> getByVideoId(String videoId) {
        List<VideoTagEntity> entityList = videoTagRepository.findByVideoIdAndVisibleTrue(videoId);
        List<VideoTagDTO> dtoList = new ArrayList<>();
        entityList.forEach(entity -> dtoList.add(toDTO(entity)));
        return dtoList;
    }

    private VideoTagDTO toDTO(VideoTagEntity entity) {
        VideoTagDTO dto = new VideoTagDTO();
        dto.setId(entity.getId());
        dto.setVideoId(entity.getVideoId());
        dto.setTagId(entity.getTagId());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setTagName(entity.getTag().getName());
        return dto;
    }
}
