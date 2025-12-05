package dasturlash.uz.you_tube.service;

import dasturlash.uz.you_tube.dto.channel.ChannelDTO;
import dasturlash.uz.you_tube.dto.playlist.*;
import dasturlash.uz.you_tube.dto.profile.ProfileDTO;
import dasturlash.uz.you_tube.entity.PlaylistEntity;
import dasturlash.uz.you_tube.enums.PlaylistStatus;
import dasturlash.uz.you_tube.enums.ProfileRole;
import dasturlash.uz.you_tube.exp.AppAccessDeniedException;
import dasturlash.uz.you_tube.exp.AppBadRequestException;
import dasturlash.uz.you_tube.mapper.PlaylistDetailMapper;
import dasturlash.uz.you_tube.mapper.PlaylistInfoMapper;
import dasturlash.uz.you_tube.mapper.PlaylistShortMapper;
import dasturlash.uz.you_tube.repository.*;
import dasturlash.uz.you_tube.util.SpringSecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
public class PlaylistService {
    @Autowired
    private PlaylistRepository playlistRepository;
    @Autowired
    private AttachService attachService;

    public PlaylistInfoDTO create(CreatePlaylistDTO dto) {
        Integer profileId = SpringSecurityUtil.getCurrentUserId();

        PlaylistEntity playlist = new PlaylistEntity();
        playlist.setChannelId(dto.getChannelId());
        playlist.setName(dto.getName());
        playlist.setDescription(dto.getDescription());
        playlist.setStatus(dto.getStatus());
        playlist.setOrderNum(dto.getOrderNum());
        playlist.setProfileId(profileId);
        playlist.setCreatedDate(LocalDateTime.now());
        
        playlistRepository.save(playlist);
        return toPlaylistInfoDTO(playlist);
    }

    public PlaylistInfoDTO updatePlaylist(String playlistId, UpdatePlaylistDTO dto) {
        Integer profileId = SpringSecurityUtil.getCurrentUserId();

        PlaylistEntity playlist = getPlaylistEntity(playlistId);
        if (playlist.getProfileId().equals(profileId)) {
            playlist.setDescription(dto.getDescription());
            playlist.setName(dto.getName());
            playlist.setOrderNum(dto.getOrderNum());
            playlist.setUpdatedDate(LocalDateTime.now());
            playlistRepository.save(playlist);
            return toPlaylistInfoDTO(playlist);
        }
        throw new AppAccessDeniedException("access denied");
    }

    public PlaylistInfoDTO changePlaylistStatus(String playlistId, PlaylistStatus status) {
        Integer profileId = SpringSecurityUtil.getCurrentUserId();

        PlaylistEntity playlist = getPlaylistEntity(playlistId);
        if (playlist.getProfileId().equals(profileId)) {
            playlist.setStatus(status);
            playlist.setUpdatedDate(LocalDateTime.now());
            playlistRepository.save(playlist);
            return toPlaylistInfoDTO(playlist);
        }
        throw new AppAccessDeniedException("access denied");
    }

    // 4. Delete Playlist (USER and OWNER, ADMIN)
    public Boolean deletePlaylist(String playlistId) {
        Integer profileId = SpringSecurityUtil.getCurrentUserId();

        PlaylistEntity playlist = getPlaylistEntity(playlistId);
        if (playlist.getProfileId().equals(profileId) || SpringSecurityUtil.checkRoleExist(ROLE_ADMIN)) {
            playlist.setVisible(false);
            playlistRepository.save(playlist);
            return true;
        }
        throw new AppAccessDeniedException("access denied");
    }
    
    // 5. Playlist Pagination (ADMIN)
    public Page<PlaylistInfoDTO> getPlaylistPagination(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PlaylistInfoMapper> playlistPage = playlistRepository.findAllByOrderByCreatedDateDesc(pageable);

        List<PlaylistInfoMapper> mappers = playlistPage.getContent();
        long total = playlistPage.getTotalElements();

        List<PlaylistInfoDTO> dtoList = new LinkedList<>();
        mappers.forEach(mapper -> dtoList.add(toPlaylistInfoDTO(mapper)));
        return new PageImpl<>(dtoList, pageable, total);
    }
    
    // 6. Playlist List By UserId (ADMIN)
    public List<PlaylistInfoDTO> getPlaylistByUserId(Integer userId) {
        List<PlaylistInfoMapper> mappers = playlistRepository.findByProfileId(userId);
        List<PlaylistInfoDTO> dtoList = new LinkedList<>();
        mappers.forEach(mapper -> dtoList.add(toPlaylistInfoDTO(mapper)));
        return dtoList;
    }
    
    // 7. Get User Playlist (murojat qilgan user ni)
    public List<PlaylistShortInfoDTO> getUserPlaylist(Integer profileId) {
        List<PlaylistShortMapper> mappers = playlistRepository.findShortMapperByProfileId(profileId);
        List<PlaylistShortInfoDTO> dtoList = new LinkedList<>();

        mappers.forEach(mapper -> dtoList.add(toPlaylistShortDTO(mapper)));
        return dtoList;
    }
    
    // 8. Get Channel Play List By ChannelKey (only Public)
    public List<PlaylistShortInfoDTO> getChannelPlayList(String channelId) {
        List<PlaylistShortMapper> mappers = playlistRepository.findByChannelId(channelId);
        List<PlaylistShortInfoDTO> dtoList = new LinkedList<>();

        mappers.forEach(mapper -> dtoList.add(toPlaylistShortDTO(mapper)));
        return dtoList;
    }
    
    // 9. Get Playlist by id
    public PlaylistDetailDTO getPlaylistById(String playlistId) {
        Optional<PlaylistDetailMapper> optional = playlistRepository.findDetailMapperById(playlistId);
        if (optional.isEmpty()) {
            throw new AppBadRequestException("playlist not found");
        }
        return toPlaylistDetailDTO(optional.get());
    }

    private PlaylistDetailDTO toPlaylistDetailDTO(PlaylistDetailMapper mapper) {
        PlaylistDetailDTO dto = new PlaylistDetailDTO();
        dto.setId(mapper.getId());
        dto.setName(mapper.getName());
        dto.setVideoCount(mapper.getVideoCount());
        dto.setLastUpdateDate(mapper.getLastUpdateDate());
        dto.setTotalViewCount(mapper.getTotalViewCount());
        return dto;
    }
    
    private PlaylistInfoDTO toPlaylistInfoDTO(PlaylistEntity entity) {
        PlaylistInfoDTO dto = new PlaylistInfoDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setOrderNum(entity.getOrderNum());
        dto.setStatus(entity.getStatus());
        return dto;
    }

    private PlaylistInfoDTO toPlaylistInfoDTO(PlaylistInfoMapper mapper) {
        PlaylistInfoDTO dto = new PlaylistInfoDTO();
        dto.setId(mapper.getId());
        dto.setName(mapper.getName());
        dto.setDescription(mapper.getDescription());
        dto.setOrderNum(mapper.getOrderNum());
        dto.setStatus(mapper.getStatus());

        dto.setChannel(toChannelDTO(mapper));
        dto.setProfile(toProfileDTO(mapper));
        return dto;
    }

    private ChannelDTO  toChannelDTO(PlaylistInfoMapper mapper) {
        ChannelDTO dto = new ChannelDTO();
        dto.setId(mapper.getChannelId());
        dto.setName(mapper.getChannelName());
        dto.setAttach(attachService.openDTO(mapper.getChannelPhotoId()));
        return dto;
    }

    private ProfileDTO toProfileDTO(PlaylistInfoMapper mapper) {
        ProfileDTO dto = new ProfileDTO();
        dto.setId(mapper.getProfileId());
        dto.setName(mapper.getProfileName());
        dto.setSurname(mapper.getProfileSurname());
        dto.setPhoto(attachService.openDTO(mapper.getProfilePhotoId()));
        return dto;
    }

    private PlaylistShortInfoDTO toPlaylistShortDTO(PlaylistShortMapper mapper) {
        PlaylistShortInfoDTO dto = new PlaylistShortInfoDTO();
        dto.setId(mapper.getId());
        dto.setName(mapper.getName());
        dto.setCreatedDate(mapper.getCreatedDate());
        dto.setChannel(toChannelDTO(mapper));
        dto.setVideoCount(mapper.getVideoCount());
        return dto;
    }

    private ChannelDTO toChannelDTO(PlaylistShortMapper mapper) {
        ChannelDTO dto = new ChannelDTO();
        dto.setId(mapper.getChannelId());
        dto.setName(mapper.getChannelName());
        return dto;
    }

    private PlaylistEntity getPlaylistEntity(String playlistId) {
        Optional<PlaylistEntity> optional = playlistRepository.findByIdAndVisibleTrue(playlistId);
        if (optional.isPresent()) {
            return optional.get();
        }
        throw new AppBadRequestException("Playlist not found");
    }
}