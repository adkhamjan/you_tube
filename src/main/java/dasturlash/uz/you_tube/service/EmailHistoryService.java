package dasturlash.uz.you_tube.service;

import dasturlash.uz.you_tube.dto.EmailHistoryDTO;
import dasturlash.uz.you_tube.entity.EmailHistoryEntity;
import dasturlash.uz.you_tube.exp.AppBadRequestException;
import dasturlash.uz.you_tube.repository.EmailHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EmailHistoryService {
    @Autowired
    private EmailHistoryRepository emailHistoryRepository;

    public void save(String email, String body, String code) {
        EmailHistoryEntity entity = new EmailHistoryEntity();
        entity.setEmail(email);
        entity.setBody(body);
        entity.setCode(code);
        entity.setCreatedDate(LocalDateTime.now());
        emailHistoryRepository.save(entity);
    }

    public boolean isSmsSendToEmail(String phone, String code) {
        EmailHistoryEntity smsHistoryEntity = getSmsByEmail(phone);
        if (smsHistoryEntity.getCreatedDate().isBefore(LocalDateTime.now())) {
            return false;
        }
        if (Duration.between(LocalDateTime.now(), smsHistoryEntity.getCreatedDate()).toMinutes() > 2) {
            return false;
        }
        if (!code.equals(smsHistoryEntity.getCode())) {
            smsHistoryEntity.setAttemptCount(smsHistoryEntity.getAttemptCount() + 1);
            emailHistoryRepository.save(smsHistoryEntity);
            return false;
        }
        return true;
    }

    public EmailHistoryEntity getSmsByEmail(String phoneNumber) {
        Optional<EmailHistoryEntity> optional = emailHistoryRepository.findTopByEmailOrderByCreatedDateDesc(phoneNumber);
        if (optional.isEmpty()) {
            throw new AppBadRequestException("Invalid phone number");
        }
        return optional.get();
    }

    public List<EmailHistoryDTO> getSmsDtoByEmail(String phone) {
        Iterable<EmailHistoryEntity> entities = emailHistoryRepository.findByEmail(phone);
        List<EmailHistoryDTO> list = new ArrayList<>();
        entities.forEach(entity -> list.add(toDto(entity)));
        return list;
    }

    public List<EmailHistoryDTO> getSmsDtoByDate(LocalDate date) {
        LocalDateTime start = LocalDateTime.of(date, LocalTime.MIN);
        LocalDateTime end = LocalDateTime.of(date, LocalTime.MAX);
        Iterable<EmailHistoryEntity> entities = emailHistoryRepository.findByDate(start, end);
        List<EmailHistoryDTO> list = new ArrayList<>();
        entities.forEach(entity -> list.add(toDto(entity)));
        return list;
    }

    public PageImpl<EmailHistoryDTO> pagination(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<EmailHistoryEntity> result = emailHistoryRepository.findAll(pageable);

        Iterable<EmailHistoryEntity> entities = result.getContent();
        long totalCount = result.getTotalElements();

        List<EmailHistoryDTO> dtoList = new ArrayList<>();
        entities.forEach(entity -> {dtoList.add(toDto(entity));});
        return new PageImpl<>(dtoList, pageable, totalCount);
    }

    private EmailHistoryDTO toDto(EmailHistoryEntity smsHistoryEntity) {
        EmailHistoryDTO smsHistoryDTO = new EmailHistoryDTO();
        smsHistoryDTO.setId(smsHistoryEntity.getId());
        smsHistoryDTO.setEmail(smsHistoryEntity.getEmail());
        smsHistoryDTO.setBody(smsHistoryEntity.getBody());
        smsHistoryDTO.setCode(smsHistoryEntity.getCode());
        smsHistoryDTO.setCreatedDate(smsHistoryEntity.getCreatedDate());
        return smsHistoryDTO;
    }
}