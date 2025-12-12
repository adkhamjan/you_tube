package dasturlash.uz.you_tube.service;

import dasturlash.uz.you_tube.dto.AttachDTO;
import dasturlash.uz.you_tube.dto.video.*;
import dasturlash.uz.you_tube.entity.ChannelEntity;
import dasturlash.uz.you_tube.entity.VideoEntity;
import dasturlash.uz.you_tube.enums.PlaylistStatus;
import dasturlash.uz.you_tube.exp.AppAccessDeniedException;
import dasturlash.uz.you_tube.exp.AppBadRequestException;
import dasturlash.uz.you_tube.mapper.video.VideoFullMapper;
import dasturlash.uz.you_tube.mapper.video.VideoPlaylistMapper;
import dasturlash.uz.you_tube.mapper.video.VideoProfileMapper;
import dasturlash.uz.you_tube.mapper.video.VideoShortMapper;
import dasturlash.uz.you_tube.repository.VideoRepository;
import dasturlash.uz.you_tube.util.SpringSecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static dasturlash.uz.you_tube.enums.ProfileRole.ROLE_ADMIN;

@Service
public class VideoService {
    @Autowired
    private VideoRepository videoRepository;
    @Autowired
    private AttachService attachService;
    @Autowired
    private ChannelService channelService;

    // 1. Create Video
    public VideoShortInfoDTO create(VideoCreateDTO dto) {
        Integer profileId = SpringSecurityUtil.getCurrentUserId();
        ChannelEntity channel = channelService.getChannelEntity(dto.getChannelId());
        if (!Objects.equals(channel.getProfileId(), profileId)) {
            throw new AppAccessDeniedException("Access denied");
        }
        AttachDTO previewAttach =  attachService.upload(dto.getPreviewAttach());
        AttachDTO attachDTO = attachService.upload(dto.getAttach());

        VideoEntity entity = new VideoEntity();
        entity.setPreviewAttachId(previewAttach.getId());
        entity.setTitle(dto.getTitle());
        entity.setCategoryId(dto.getCategoryId());
        entity.setAttachId(attachDTO.getId());
        entity.setDescription(dto.getDescription());
        entity.setType(dto.getType());
        entity.setChannelId(dto.getChannelId());
        entity.setCreatedDate(LocalDateTime.now());
        entity.setStatus(PlaylistStatus.PRIVATE);

        videoRepository.save(entity);
        return toShortDTO(entity);
    }

    // 2. Update Detail
    public VideoShortInfoDTO update(String id, VideoUpdateDTO dto) {
        Integer profileId = SpringSecurityUtil.getCurrentUserId();
        VideoEntity entity = get(id);
        if (!entity.getChannel().getProfileId().equals(profileId)) {
            throw new AppAccessDeniedException("Access denied");
        }

        entity.setTitle(dto.getTitle());
        entity.setCategoryId(dto.getCategoryId());
        entity.setDescription(dto.getDescription());
        entity.setType(dto.getType());

        videoRepository.save(entity);
        return toShortDTO(entity);
    }

    // 3. Change Status
    public VideoShortInfoDTO changeStatus(String id, PlaylistStatus status) {
        Integer profileId = SpringSecurityUtil.getCurrentUserId();
        VideoEntity entity = get(id);
        if (!entity.getChannel().getProfileId().equals(profileId)) {
            throw new AppAccessDeniedException("Access denied");
        }
        if (status == entity.getStatus()) {
            throw new AppBadRequestException("You are not allowed to change status");
        }
        entity.setStatus(status);
        if (status == PlaylistStatus.PUBLIC) {
            entity.setPublishedDate(LocalDateTime.now());
        }
        videoRepository.save(entity);

        return toShortDTO(entity);
    }

    // 4. Increase View Count
    public void increaseViewCount(String id) {
        VideoEntity entity = get(id);
        entity.setViewCount(entity.getViewCount() + 1);
        videoRepository.save(entity);
    }

    // 5. Pagination by CategoryId
    public PageImpl<VideoShortInfoDTO> paginationByCategoryId(Integer categoryId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        PageImpl<VideoShortMapper> resultPage = videoRepository.findByCategoryId(categoryId, pageable);

        return toPageImplDTO(resultPage, pageable);
    }

    // 6. Search by title
    public PageImpl<VideoShortInfoDTO> search(String title, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        PageImpl<VideoShortMapper> resultPage = videoRepository.findByTitleContainingIgnoreCase(title, pageable);

        return toPageImplDTO(resultPage, pageable);
    }

    // 7. Get by tagId
    public PageImpl<VideoShortInfoDTO> paginationByTagId(Integer tagId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        PageImpl<VideoShortMapper> resultPage = videoRepository.findByTagId(tagId, pageable);

        return toPageImplDTO(resultPage, pageable);
    }

    private PageImpl<VideoShortInfoDTO> toPageImplDTO(PageImpl<VideoShortMapper> resultPage, Pageable pageable) {
        List<VideoShortMapper> mapperList = resultPage.getContent();
        long totalCount = resultPage.getTotalElements();

        List<VideoShortInfoDTO> dtoList = new LinkedList<>();
        mapperList.forEach(mapper -> dtoList.add(toShortDTO(mapper)));
        return new PageImpl<>(dtoList, pageable, totalCount);
    }

    // 8. Get by id (PRIVATE → only owner/admin)
    public VideoFullInfoDTO getDTO(String id) {
        VideoEntity video = get(id);
        if (video.getStatus().equals(PlaylistStatus.PRIVATE)) {
            Integer profileId = SpringSecurityUtil.getCurrentUserId();
            if (!video.getChannel().getProfileId().equals(profileId) & !SpringSecurityUtil.checkRoleExist(ROLE_ADMIN)) {
                throw new AppAccessDeniedException("Access denied");
            }
        }
        VideoFullMapper mapper = videoRepository.findByVideoId(id);
        return toFullDTO(mapper);
    }

    public VideoEntity get(String id) {
        Optional<VideoEntity> optional = videoRepository.findByIdAndVisibleTrue(id);
        if (optional.isEmpty()) {
            throw new AppBadRequestException("Video not found");
        }
        return optional.get();
    }

    // 9. Get all videos (ADMIN)
    public PageImpl<VideoProfileInfoDTO> getAdminPagination(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        PageImpl<VideoProfileMapper> resultPage = videoRepository.getAll(pageable);

        List<VideoProfileMapper> mapperList = resultPage.getContent();
        long totalCount = resultPage.getTotalElements();

        List<VideoProfileInfoDTO> dtoList = new LinkedList<>();
        mapperList.forEach(mapper -> dtoList.add(toVideoProfileInfoDTO(mapper)));
        return new PageImpl<>(dtoList, pageable, totalCount);
    }

    // 10. Get channel videos list pagination
    public PageImpl<VideoPlayListInfoDTO> channelVideos(String channelId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        PageImpl<VideoPlaylistMapper> resultPage = videoRepository.findByChannelIdOrderByCreatedDateDesc(channelId, pageable);

        List<VideoPlaylistMapper> mapperList = resultPage.getContent();
        long totalCount = resultPage.getTotalElements();

        List<VideoPlayListInfoDTO> dtoList = new LinkedList<>();
        mapperList.forEach(mapper -> dtoList.add(toVideoProfileInfoDTO(mapper)));
        return new PageImpl<>(dtoList, pageable, totalCount);
    }

    private VideoShortInfoDTO toShortDTO(VideoEntity entity) {
        VideoShortInfoDTO dto = new VideoShortInfoDTO();

        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setPreviewAttach(attachService.openDTO(entity.getPreviewAttachId()));
        dto.setChannelId(entity.getChannelId());
        dto.setViewCount(entity.getViewCount());
        return dto;
    }

    private VideoShortInfoDTO toShortDTO(VideoShortMapper mapper) {
        VideoShortInfoDTO dto = new VideoShortInfoDTO();

        dto.setId(mapper.getId());
        dto.setTitle(mapper.getTitle());
        dto.setPreviewAttach(attachService.openDTO(mapper.getPreviewAttachId()));
        dto.setChannelId(mapper.getChannelId());
        dto.setChannelName(mapper.getChannelName());
        dto.setChannelAttach(attachService.openDTO(mapper.getChannelPhotoId()));
        dto.setViewCount(mapper.getViewCount());
        if (mapper.getPublishedDate() != null) {
            dto.setPublishedDate(mapper.getPublishedDate());
            dto.setDuration((int) Duration.between(mapper.getPublishedDate(), LocalDateTime.now()).getSeconds());
        }

        return dto;
    }

    private VideoProfileInfoDTO toVideoProfileInfoDTO(VideoProfileMapper mapper) {
        VideoProfileInfoDTO dto = new VideoProfileInfoDTO();

        dto.setId(mapper.getId());
        dto.setTitle(mapper.getTitle());
        dto.setProfileId(mapper.getProfileId());
        dto.setProfileName(mapper.getProfileName());
        dto.setProfileSurname(mapper.getProfileSurname());
        dto.setPreviewAttach(attachService.openDTO(mapper.getPreviewAttachId()));
        dto.setChannelId(mapper.getChannelId());
        dto.setChannelName(mapper.getChannelName());
        dto.setChannelAttach(attachService.openDTO(mapper.getChannelPhotoId()));
        dto.setViewCount(mapper.getViewCount());
        if (mapper.getPublishedDate() != null) {
            dto.setPublishedDate(mapper.getPublishedDate());
            dto.setDuration((int) Duration.between(mapper.getPublishedDate(), LocalDateTime.now()).getSeconds());
        }

        return dto;
    }

    private VideoPlayListInfoDTO toVideoProfileInfoDTO(VideoPlaylistMapper mapper) {
        VideoPlayListInfoDTO dto = new VideoPlayListInfoDTO();

        dto.setId(mapper.getId());
        dto.setTitle(mapper.getTitle());
        dto.setViewCount(mapper.getViewCount());
        dto.setPreviewAttach(attachService.openDTO(mapper.getPreviewAttachId()));
        if (mapper.getPublishedDate() != null) {
            dto.setPublishedDate(mapper.getPublishedDate());
            dto.setDuration((int) Duration.between(mapper.getPublishedDate(), LocalDateTime.now()).getSeconds());
        }

        return dto;
    }

    private VideoFullInfoDTO toFullDTO(VideoFullMapper mapper) {
        VideoFullInfoDTO dto = new VideoFullInfoDTO();

        dto.setId(mapper.getId());
        dto.setTitle(mapper.getTitle());
        dto.setPreviewAttach(attachService.openDTO(mapper.getPreviewAttachId()));
        dto.setAttach(attachService.openDTO(mapper.getAttachId()));
        dto.setChannelId(mapper.getChannelId());
        dto.setChannelName(mapper.getChannelName());
        dto.setChannelAttach(attachService.openDTO(mapper.getChannelAttachId()));
        dto.setViewCount(mapper.getViewCount());
        dto.setCategoryId(mapper.getCategoryId());
        dto.setCategoryName(mapper.getCategoryName());
        dto.setDescription(mapper.getDescription());
        dto.setLikeCount(mapper.getLikeCount());
        dto.setDislikeCount(mapper.getDislikeCount());
        dto.setTagList(mapper.getTagList());
        dto.setSharedCount(mapper.getSharedCount());
        if (mapper.getPublishedDate() != null) {
            dto.setPublishedDate(mapper.getPublishedDate());
            dto.setDuration((int) Duration.between(mapper.getPublishedDate(), LocalDateTime.now()).getSeconds());
        }

        return dto;
    }
}
