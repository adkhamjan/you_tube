package dasturlash.uz.you_tube.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TagDTO {
    private Integer id;
    
    @NotBlank(message = "Tag name is required")
    private String name;
    
    private LocalDateTime createdDate;
    private Boolean visible;
}