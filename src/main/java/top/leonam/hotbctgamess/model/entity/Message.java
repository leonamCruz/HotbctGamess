package top.leonam.hotbctgamess.model.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;

@Entity
@Table(
        indexes = {
                @Index(name = "idx_message_timestamp", columnList = "timestamp"),
                @Index(name = "idx_message_user", columnList = "user_id"),
                @Index(name = "idx_message_channel", columnList = "channel_id")
        })
@Data
public class Message {

    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_id", nullable = false)
    private Channel channel;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private Instant timestamp;

    private boolean edited;

    private boolean deleted;

    private int length;

    private int mentionCount;

    private int emojiCount;

    private int linkCount;

    private int stickerCount;

    private int attachmentCount;

}

