package dasturlash.uz.you_tube.service;

import dasturlash.uz.you_tube.dto.AttachDTO;
import dasturlash.uz.you_tube.dto.channel.ChannelDTO;
import dasturlash.uz.you_tube.dto.channel.CreateChannelDTO;
import dasturlash.uz.you_tube.dto.channel.UpdateChannelDTO;
import dasturlash.uz.you_tube.entity.ChannelEntity;
import dasturlash.uz.you_tube.enums.ChannelStatus;
import dasturlash.uz.you_tube.enums.ProfileRole;
import dasturlash.uz.you_tube.exp.AppAccessDeniedException;
import dasturlash.uz.you_tube.exp.AppBadRequestException;
import dasturlash.uz.you_tube.repository.ChannelRepository;
import dasturlash.uz.you_tube.util.SpringSecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ChannelService {
    @Autowired
    private ChannelRepository channelRepository;
    @Autowired
    private AttachService attachService;

    public ChannelDTO create(CreateChannelDTO dto) {
        if (channelRepository.existsByName(dto.getName())) {
            throw new AppBadRequestException("Channel with name " + dto.getName() + " already exists");
        }
        ChannelEntity channel = new ChannelEntity();
        channel.setName(dto.getName());
        channel.setDescription(dto.getDescription());
        channel.setProfileId(SpringSecurityUtil.getCurrentUserId());
        channel.setStatus(ChannelStatus.ACTIVE);
        
        channelRepository.save(channel);
        return toDTO(channel);
    }

    public ChannelDTO updateChannel(UUID channelId, UpdateChannelDTO dto) {
        Integer currentUserId = SpringSecurityUtil.getCurrentUserId();
        ChannelEntity entity = getChannelEntity(channelId);

        if (currentUserId.equals(entity.getProfileId())) {
            entity.setName(dto.getName());
            entity.setDescription(dto.getDescription());
            entity.setUpdatedDate(LocalDateTime.now());
            channelRepository.save(entity);
            return toDTO(entity);
        }
        throw new AppAccessDeniedException("You are not allowed to update this channel");
    }

    public ChannelDTO updateChannelPhoto(UUID channelId, MultipartFile photo) {
        Integer currentUserId = SpringSecurityUtil.getCurrentUserId();
        ChannelEntity entity = getChannelEntity(channelId);

        if (currentUserId.equals(entity.getProfileId())) {
            if (entity.getPhotoId() != null) {
                attachService.delete(entity.getPhotoId());
            }
            AttachDTO attachDTO = attachService.upload(photo);
            entity.setPhotoId(attachDTO.getId());

            entity.setUpdatedDate(LocalDateTime.now());
            channelRepository.save(entity);
            return toDTO(entity);
        }
        throw new AppAccessDeniedException("You are not allowed to update this channel");
    }
    
    // 4. Update Channel banner (USER and OWNER)
    public ChannelDTO updateChannelBanner(UUID channelId, MultipartFile banner) {
        Integer currentUserId = SpringSecurityUtil.getCurrentUserId();
        ChannelEntity entity = getChannelEntity(channelId);

        if (currentUserId.equals(entity.getProfileId())) {
            if (entity.getBannerId() != null) {
                attachService.delete(entity.getBannerId());
            }
            AttachDTO attachDTO = attachService.upload(banner);
            entity.setBannerId(attachDTO.getId());

            entity.setUpdatedDate(LocalDateTime.now());
            channelRepository.save(entity);
            return toDTO(entity);
        }
        throw new AppAccessDeniedException("You are not allowed to update this channel");
    }

    public Page<ChannelDTO> getChannelPagination(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ChannelEntity> channelPage = channelRepository.findAllByOrderByCreatedDateDesc(pageable);

        List<ChannelEntity> entities = channelPage.getContent();
        long totalElements = channelPage.getTotalElements();

        List<ChannelDTO> dtoList = new LinkedList<>();
        entities.forEach(entity -> dtoList.add(toDTO(entity)));

        return new PageImpl<>(dtoList, pageable, totalElements);
    }

    public ChannelDTO getChannelById(UUID channelId) {
        ChannelEntity entity = getChannelEntity(channelId);
        return toDTO(entity);
    }

    public ChannelDTO changeChannelStatus(UUID channelId, ChannelStatus status) {
        Integer currentUserId = SpringSecurityUtil.getCurrentUserId();
        ChannelEntity entity = getChannelEntity(channelId);

        if (SpringSecurityUtil.checkRoleExist(ProfileRole.ROLE_ADMIN) || currentUserId.equals(entity.getProfileId())) {
            if (entity.getStatus() == status) {
                throw new AppBadRequestException("already status of this channel");
            }
            entity.setStatus(status);

            entity.setUpdatedDate(LocalDateTime.now());
            channelRepository.save(entity);
            return toDTO(entity);
        }
        throw new AppAccessDeniedException("You are not allowed to update this channel");
    }

    public List<ChannelDTO> getUserChannelList(Integer profileId) {
        List<ChannelEntity> channels = channelRepository.findByProfileIdAndVisibleTrueOrderByCreatedDateDesc(profileId);
        List<ChannelDTO> dtoList = new LinkedList<>();
        channels.forEach(entity -> dtoList.add(toDTO(entity)));
        return dtoList;
    }
    
    private ChannelDTO toDTO(ChannelEntity channel) {
        ChannelDTO dto = new ChannelDTO();
        dto.setId(channel.getId());
        dto.setName(channel.getName());
        dto.setDescription(channel.getDescription());
        dto.setProfileId(channel.getProfileId());
        dto.setVisible(channel.getVisible());
        if (channel.getBannerId() != null) {
            dto.setBanner(attachService.openDTO(channel.getBannerId()));
        }
        dto.setCreatedDate(channel.getCreatedDate());
        dto.setUpdatedDate(channel.getUpdatedDate());
        dto.setStatus(channel.getStatus());
        if (channel.getPhotoId() != null) {
            dto.setAttach(attachService.openDTO(channel.getPhotoId()));
        }
        return dto;
    }

    private ChannelEntity getChannelEntity(UUID channelId) {
        Optional<ChannelEntity> optional = channelRepository.findByIdAndVisibleTrue(channelId);
        if (optional.isPresent()) {
            return optional.get();
        }
        throw new AppBadRequestException("Channel not found");
    }
}