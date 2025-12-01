package dasturlash.uz.you_tube.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "tags")
public class TagEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "visible")
    private Boolean visible = Boolean.TRUE;

    @CreationTimestamp
    @Column(name = "created_date")
    private LocalDateTime createdDate;
}