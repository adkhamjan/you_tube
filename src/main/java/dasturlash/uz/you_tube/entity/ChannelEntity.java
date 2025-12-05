package dasturlash.uz.you_tube.entity;

import dasturlash.uz.you_tube.enums.ChannelStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "channel")
public class ChannelEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    
    @Column(name = "name")
    private String name;

    @Column(name = "photo_id")
    private String photoId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "photo_id", insertable = false, updatable = false)
    private AttachEntity photo;
    
    @Column(length = 500)
    private String description;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ChannelStatus status;

    @Column(name = "banner_id")
    private String bannerId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "banner_id", insertable = false, updatable = false)
    private AttachEntity banner;
    
    @Column(name = "profile_id")
    private Integer profileId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", insertable = false, updatable = false)
    private ProfileEntity profile;
    
    @Column(name = "visible")
    private Boolean visible = true;
    
    @Column(name = "created_date")
    private LocalDateTime createdDate = LocalDateTime.now();
    
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

    @Column(name = "video_count")
    private Integer videoCount = 0;
}