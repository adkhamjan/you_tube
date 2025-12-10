package dasturlash.uz.you_tube.service;

import dasturlash.uz.you_tube.entity.VideoEntity;
import dasturlash.uz.you_tube.exp.AppBadRequestException;
import dasturlash.uz.you_tube.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VideoService {
    @Autowired
    private VideoRepository videoRepository;

    public VideoEntity get(String id) {
        Optional<VideoEntity> optional = videoRepository.findByIdAndVisibleTrue(id);
        if (optional.isPresent()) {
            return optional.get();
        }
        throw new AppBadRequestException("not found video");
    }
}
