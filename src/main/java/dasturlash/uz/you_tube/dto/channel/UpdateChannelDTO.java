package dasturlash.uz.you_tube.dto.channel;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateChannelDTO {
    @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters")
    private String name;
    
    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;
}