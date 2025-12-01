package dasturlash.uz.you_tube.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "attach")
public class AttachEntity {
    @Id
    private String id;
    
    @Column(name = "origin_name", nullable = false)
    private String originName;
    
    @Column(name = "size")
    private Long size;
    
    @Column(name = "type")
    private String type;
    
    @Column(name = "path")
    private String path;

    @Column(name = "duration")
    private Long duration;
    
    @Column(name = "created_date")
    @CreationTimestamp
    private LocalDateTime createdDate;

    @Column(name = "visible")
    private Boolean visible = Boolean.TRUE;
}