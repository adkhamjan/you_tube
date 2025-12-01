package dasturlash.uz.you_tube.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TagDTO {
    private Integer id;
    
    @NotBlank(message = "Tag name is required")
    private String name;
    
    private LocalDateTime createdDate;
    private Boolean visible;
}