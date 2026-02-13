package top.leonam.hotbctgamess.model.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Entity
@Data
@Table(name = "users")
public class User {
    @Id
    private Long id;

    @Column(nullable = false)
    private String username;

    private String discriminator;

    private String avatarUrl;

    @Column(nullable = false)
    private Instant createdAt;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Message> messages;
}
