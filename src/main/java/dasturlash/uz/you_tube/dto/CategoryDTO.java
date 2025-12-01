package dasturlash.uz.you_tube.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryDTO {
    private Integer id;
    @NotBlank(message = "CategoryKey required")
    private String categoryKey;
    @NotBlank(message = "Name required")
    private String name;
    private LocalDateTime createdDate;
    private Boolean visible;
}