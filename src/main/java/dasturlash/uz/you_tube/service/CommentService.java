package dasturlash.uz.you_tube.service;

import dasturlash.uz.you_tube.dto.comment.CommentCreateDTO;
import dasturlash.uz.you_tube.dto.comment.CommentProfileDTO;
import dasturlash.uz.you_tube.dto.comment.CommentVideoDTO;
import dasturlash.uz.you_tube.entity.CommentEntity;
import dasturlash.uz.you_tube.exp.AppAccessDeniedException;
import dasturlash.uz.you_tube.exp.AppBadRequestException;
import dasturlash.uz.you_tube.mapper.CommentMapper;
import dasturlash.uz.you_tube.repository.CommentRepository;
import dasturlash.uz.you_tube.util.SpringSecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static dasturlash.uz.you_tube.enums.ProfileRole.ROLE_ADMIN;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    AttachService attachService;

    public CommentProfileDTO create(CommentCreateDTO createDTO) {
        CommentEntity entity = new CommentEntity();
        entity.setContent(createDTO.getContent());
        entity.setVideoId(createDTO.getVideoId());
        entity.setCreatedDate(LocalDateTime.now());
        entity.setProfileId(SpringSecurityUtil.getCurrentUserId());
        entity.setLikeCount(0L);
        entity.setDislikeCount(0L);
        if (createDTO.getReplyId() != null) {
            entity.setReplyId(createDTO.getReplyId());
        }

        commentRepository.save(entity);
        return toDTO(entity);
    }

    public CommentProfileDTO update(Integer commentId, CommentCreateDTO updateDTO) {
        CommentEntity entity = get(commentId);
        if (!entity.getProfileId().equals(SpringSecurityUtil.getCurrentUserId())) {
            throw new AppAccessDeniedException("Access denied");
        }
        entity.setContent(updateDTO.getContent());
        entity.setVideoId(updateDTO.getVideoId());
        entity.setUpdateDate(LocalDateTime.now());
        commentRepository.save(entity);

        return toDTO(entity);
    }

    public Boolean delete(Integer commentId) {
        CommentEntity entity = get(commentId);
        if (!entity.getProfileId().equals(SpringSecurityUtil.getCurrentUserId()) & !SpringSecurityUtil.checkRoleExist(ROLE_ADMIN)) {
            throw new  AppAccessDeniedException("Access denied");
        }
        entity.setVisible(false);
        commentRepository.save(entity);
        return true;
    }

    public PageImpl<CommentProfileDTO> pagination(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        PageImpl<CommentMapper> resultPage = commentRepository.pagination(pageable);

        List<CommentMapper> mapperList = resultPage.getContent();
        long totalCount = resultPage.getTotalElements();

        List<CommentProfileDTO> dtoList = new LinkedList<>();
        mapperList.forEach(mapper -> dtoList.add(toDTO(mapper)));
        return new PageImpl<>(dtoList, PageRequest.of(page, size), totalCount);
    }

    public List<CommentVideoDTO> getByProfileId(Integer profileId) {
        List<CommentMapper> mapperList = commentRepository.getByProfileId(profileId);

        List<CommentVideoDTO> dtoList = new LinkedList<>();
        mapperList.forEach(mapper -> dtoList.add(toVideoDTO(mapper)));
        return dtoList;
    }

    public List<CommentVideoDTO> getByOwner() {
        Integer profileId = SpringSecurityUtil.getCurrentUserId();
        return getByProfileId(profileId);
    }

    public List<CommentProfileDTO> getByVideoId(String videoId) {
        List<CommentMapper> mapperList = commentRepository.getByVideoId(videoId);

        List<CommentProfileDTO> dtoList = new LinkedList<>();
        mapperList.forEach(mapper -> dtoList.add(toDTO(mapper)));
        return dtoList;
    }

    public List<CommentProfileDTO> getReplyByCommentId(Integer commentId) {
        List<CommentMapper> mapperList = commentRepository.getReplyByCommentId(commentId);

        List<CommentProfileDTO> dtoList = new LinkedList<>();
        mapperList.forEach(mapper -> dtoList.add(toDTO(mapper)));
        return dtoList;
    }

    private CommentProfileDTO toDTO(CommentMapper mapper) {
        CommentProfileDTO dto = new CommentProfileDTO();
        dto.setId(mapper.getId());
        dto.setCreatedDate(mapper.getCreatedDate());
        dto.setContent(mapper.getContent());
        dto.setLikeCount(mapper.getLikeCount());
        dto.setDislikeCount(mapper.getDislikeCount());
        dto.setProfileId(mapper.getProfileId());
        dto.setProfileName(mapper.getProfileName());
        dto.setProfileSurname(mapper.getProfileSurname());
        if (mapper.getProfileImageId() != null) {
            dto.setProfileImage(attachService.openDTO(mapper.getProfileImageId()));
        }
        return dto;
    }

    private CommentVideoDTO toVideoDTO(CommentMapper mapper) {
        CommentVideoDTO dto = new CommentVideoDTO();
        dto.setId(mapper.getId());
        dto.setCreatedDate(mapper.getCreatedDate());
        dto.setContent(mapper.getContent());
        dto.setLikeCount(mapper.getLikeCount());
        dto.setDislikeCount(mapper.getDislikeCount());
        dto.setVideoId(mapper.getVideoId());
        dto.setDuration(mapper.getDuration());
        dto.setVideoTitle(mapper.getVideoTitle());
        dto.setPreviewAttachId(mapper.getPreviewAttachId());
        return dto;
    }

    private CommentProfileDTO toDTO(CommentEntity entity) {
        CommentProfileDTO dto = new CommentProfileDTO();
        dto.setId(entity.getId());
        dto.setContent(entity.getContent());
        dto.setCreatedDate(entity.getCreatedDate());

        return dto;
    }

    public CommentEntity get(Integer id) {
        Optional<CommentEntity> optional = commentRepository.findByIdAndVisibleTrue(id);
        if (optional.isEmpty()) {
            throw new AppBadRequestException("Comment not found");
        }
        return optional.get();
    }
}
