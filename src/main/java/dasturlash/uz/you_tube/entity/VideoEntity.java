package dasturlash.uz.you_tube.entity;

import dasturlash.uz.you_tube.enums.PlaylistStatus;
import dasturlash.uz.you_tube.enums.VideoTypeEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "video")
public class VideoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "preview_attach_id")
    private String previewAttachId;
    @ManyToOne
    @JoinColumn(name = "preview_attach_id", insertable = false, updatable = false)
    private AttachEntity previewAttach;

    @Column(name = "title")
    private String title;

    @Column(name = "category_id")
    private Integer categoryId;
    @ManyToOne
    @JoinColumn(name = "category_id", insertable = false, updatable = false)
    private CategoryEntity category;

    @Column(name = "attach_id")
    private String attachId;
    @ManyToOne
    @JoinColumn(name = "attach_id", insertable = false, updatable = false)
    private AttachEntity attach;

    @Column(name = "created_date")
    private LocalDateTime createdDate;
    @Column(name = "published_date")
    private LocalDateTime publishedDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private PlaylistStatus status;    // private, public

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private VideoTypeEnum type;        // video, short

    @Column(name = "view_count")
    private Long viewCount = 0L;
    @Column(name = "shared_count")
    private Long sharedCount = 0L;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Column(name = "channel_id")
    private String channelId;
    @ManyToOne
    @JoinColumn(name = "channel_id",  insertable = false, updatable = false)
    private ChannelEntity channel;

    @Column(name = "like_count")
    private Long likeCount = 0L;
    @Column(name = "dislike_count")
    private Long dislikeCount = 0L;
}