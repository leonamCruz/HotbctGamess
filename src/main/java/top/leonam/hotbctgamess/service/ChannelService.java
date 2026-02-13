package top.leonam.hotbctgamess.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.leonam.hotbctgamess.model.entity.Channel;
import top.leonam.hotbctgamess.repository.ChannelRepository;

import java.time.Instant;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ChannelService {

    private final ChannelRepository channelRepository;

    @Transactional
    public Channel getOrCreate(Long id, String name) {
        return channelRepository.findById(id)
                .orElseGet(() -> {
                    Channel channel = new Channel();
                    channel.setId(id);
                    channel.setName(name);
                    channel.setCreatedAt(Instant.now());
                    return channelRepository.save(channel);
                });
    }
}

