package top.leonam.hotbctgamess.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import top.leonam.hotbctgamess.model.entity.Channel;

public interface ChannelRepository extends JpaRepository<Channel,Long> {
}
