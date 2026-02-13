package top.leonam.hotbctgamess.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.leonam.hotbctgamess.model.entity.User;
import top.leonam.hotbctgamess.repository.UserRepository;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public User getOrCreate(Long id,
                            String username,
                            String discriminator,
                            String avatarUrl) {

        return userRepository.findById(id)
                .map(user -> {
                    user.setUsername(username);
                    user.setDiscriminator(discriminator);
                    user.setAvatarUrl(avatarUrl);
                    return user;
                })
                .orElseGet(() -> {
                    User user = new User();
                    user.setId(id);
                    user.setUsername(username);
                    user.setDiscriminator(discriminator);
                    user.setAvatarUrl(avatarUrl);
                    user.setCreatedAt(Instant.now());
                    return userRepository.save(user);
                });
    }

}
