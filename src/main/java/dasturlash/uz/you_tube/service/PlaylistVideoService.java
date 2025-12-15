package dasturlash.uz.you_tube.service;

import dasturlash.uz.you_tube.dto.playlist_video.PlaylistVideoDTO;
import dasturlash.uz.you_tube.dto.playlist_video.PlaylistVideoDeleteDTO;
import dasturlash.uz.you_tube.dto.playlist_video.PlaylistVideoInfoDTO;
import dasturlash.uz.you_tube.entity.PlaylistEntity;
import dasturlash.uz.you_tube.entity.PlaylistVideoEntity;
import dasturlash.uz.you_tube.entity.VideoEntity;
import dasturlash.uz.you_tube.exp.AppAccessDeniedException;
import dasturlash.uz.you_tube.exp.AppBadRequestException;
import dasturlash.uz.you_tube.mapper.PlaylistVideoInfoMapper;
import dasturlash.uz.you_tube.repository.PlaylistRepository;
import dasturlash.uz.you_tube.repository.PlaylistVideoRepository;
import dasturlash.uz.you_tube.repository.VideoRepository;
import dasturlash.uz.you_tube.util.SpringSecurityUtil;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class PlaylistVideoService {
    @Autowired
    private PlaylistVideoRepository playlistVideoRepository;
    @Autowired
    private PlaylistRepository playlistRepository;
    @Autowired
    private VideoRepository videoRepository;
    @Autowired
    private AttachService attachService;

    @Transactional
    public PlaylistVideoDTO create(PlaylistVideoDTO dto) {
        Integer profileId = SpringSecurityUtil.getCurrentUserId();

        // Check if playlist exists and user is owner
        PlaylistEntity playlist = playlistRepository.findByIdAndVisibleTrue(dto.getPlaylistId())
            .orElseThrow(() -> new AppBadRequestException("Playlist not found"));

        if (!playlist.getChannel().getProfileId().equals(profileId)) {
            throw new AppAccessDeniedException("You are not the owner of this playlist");
        }

        // Check if video exists
        VideoEntity video = videoRepository.findByIdAndVisibleTrue(dto.getVideoId())
            .orElseThrow(() -> new AppBadRequestException("Video not found"));

        // Check if video already in playlist
        if (playlistVideoRepository.existsByPlaylistIdAndVideoId(dto.getPlaylistId(), dto.getVideoId())) {
            throw new AppBadRequestException("Video already exists in this playlist");
        }

        PlaylistVideoEntity entity = new PlaylistVideoEntity();
        entity.setPlaylistId(dto.getPlaylistId());
        entity.setVideoId(dto.getVideoId());
        entity.setOrderNum(dto.getOrderNum());
        entity.setCreatedDate(LocalDateTime.now());

        playlistVideoRepository.save(entity);
        dto.setId(entity.getId());
        return dto;
    }

    @Transactional
    public PlaylistVideoEntity update(PlaylistVideoDTO dto) {
        Integer profileId = SpringSecurityUtil.getCurrentUserId();

        // Check if playlist exists and user is owner
        PlaylistEntity playlist = playlistRepository.findByIdAndVisibleTrue(dto.getPlaylistId())
                .orElseThrow(() -> new AppBadRequestException("Playlist not found"));

        if (!playlist.getChannel().getProfileId().equals(profileId)) {
            throw new AppAccessDeniedException("You are not the owner of this playlist");
        }

        // Find playlist video
        PlaylistVideoEntity entity = playlistVideoRepository
            .findByPlaylistIdAndVideoId(dto.getPlaylistId(), dto.getVideoId())
            .orElseThrow(() -> new AppBadRequestException("Video not found in this playlist"));

        entity.setOrderNum(dto.getOrderNum());
        return playlistVideoRepository.save(entity);
    }

    @Transactional
    public void delete(PlaylistVideoDeleteDTO dto) {
        Integer profileId = SpringSecurityUtil.getCurrentUserId();

        // Check if playlist exists and user is owner
        PlaylistEntity playlist = playlistRepository.findByIdAndVisibleTrue(dto.getPlaylistId())
                .orElseThrow(() -> new AppBadRequestException("Playlist not found"));

        if (!playlist.getChannel().getProfileId().equals(profileId)) {
            throw new AppAccessDeniedException("You are not the owner of this playlist");
        }

        // Find and delete playlist video
        PlaylistVideoEntity entity = playlistVideoRepository
            .findByPlaylistIdAndVideoId(dto.getPlaylistId(), dto.getVideoId())
            .orElseThrow(() -> new AppBadRequestException("Video not found in this playlist"));

        playlistVideoRepository.delete(entity);
    }

    public PageImpl<PlaylistVideoInfoDTO> getVideosByPlaylistId(String playlistId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        PageImpl<PlaylistVideoInfoMapper> resultPage = playlistVideoRepository.findPublishedVideosByPlaylistId(playlistId, pageable);

        List<PlaylistVideoInfoMapper> mapperList = resultPage.getContent();
        long total = resultPage.getTotalElements();

        List<PlaylistVideoInfoDTO> dtoList = new ArrayList<>();
        mapperList.forEach(mapper -> dtoList.add(toPlaylistVideoInfoDTO(mapper)));
        return new PageImpl<>(dtoList, pageable, total);
    }

    private PlaylistVideoInfoDTO toPlaylistVideoInfoDTO(PlaylistVideoInfoMapper mapper) {
        PlaylistVideoInfoDTO dto = new PlaylistVideoInfoDTO();
        dto.setPlaylistVideoId(mapper.getPlaylistVideoId());
        dto.setOrderNum(mapper.getOrderNum());
        dto.setCreatedDate(mapper.getCreatedDate());

        // Video info
        dto.setVideoId(mapper.getVideoId());
        dto.setVideoTitle(mapper.getVideoTitle());
        dto.setVideoDuration((int) Duration.between(mapper.getVideoPublishedDate(), LocalDateTime.now()).getSeconds());
        if (mapper.getPreviewAttachId() != null) {
            dto.setPreviewAttach(attachService.openDTO(mapper.getPreviewAttachId()));
        }

        // Channel info
        dto.setChannelId(mapper.getChannelId());
        dto.setChannelName(mapper.getChannelName());
        return dto;
    }
}