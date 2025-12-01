package dasturlash.uz.you_tube.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class EmailHistoryDTO {
    private String id;
    private String email;
    private String body;
    private String code;
    private Integer attemptCount;
    private LocalDateTime createdDate;
}