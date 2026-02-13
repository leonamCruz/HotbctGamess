package top.leonam.hotbctgamess.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.leonam.hotbctgamess.model.entity.Channel;
import top.leonam.hotbctgamess.model.entity.Message;
import top.leonam.hotbctgamess.model.entity.User;
import top.leonam.hotbctgamess.repository.MessageRepository;

import java.time.Instant;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserService userService;
    private final ChannelService channelService;

    @Transactional
    public void saveMessage(Long messageId,
                            Long userId,
                            String username,
                            String discriminator,
                            String avatarUrl,
                            Long channelId,
                            String channelName,
                            String content,
                            Instant timestamp,
                            int mentionCount,
                            int linkCount,
                            int emojiCount,
                            int stickerCount,
                            int attachmentCount) {

        User user = userService.getOrCreate(
                userId,
                username,
                discriminator,
                avatarUrl
        );

        Channel channel = channelService.getOrCreate(channelId, channelName);

        Message message = new Message();
        message.setId(messageId);
        message.setUser(user);
        message.setChannel(channel);
        message.setContent(content);
        message.setTimestamp(timestamp);
        message.setLength(content != null ? content.length() : 0);
        message.setEdited(false);
        message.setDeleted(false);

        message.setMentionCount(mentionCount);
        message.setLinkCount(linkCount);
        message.setEmojiCount(emojiCount);
        message.setStickerCount(stickerCount);
        message.setAttachmentCount(attachmentCount);

        messageRepository.save(message);
    }

    @Transactional
    public void updateMessage(Long messageId, String newContent) {
        messageRepository.findById(messageId).ifPresent(message -> {
            message.setContent(newContent);
            message.setEdited(true);
            message.setLength(newContent != null ? newContent.length() : 0);
            message.setMentionCount(countMentions(newContent));
            message.setLinkCount(countLinks(newContent));
            message.setEmojiCount(countEmojis(newContent));
        });
    }

    @Transactional
    public void markAsDeleted(Long messageId) {
        messageRepository.findById(messageId)
                .ifPresent(message -> message.setDeleted(true));
    }

    private int countMentions(String content) {
        if (content == null) return 0;
        Pattern pattern = Pattern.compile("<@!?\\d+>|<@&\\d+>|<#\\d+>");
        Matcher matcher = pattern.matcher(content);
        int count = 0;
        while (matcher.find()) {
            count++;
        }
        return count;
    }

    private int countLinks(String content) {
        if (content == null) return 0;
        Pattern pattern = Pattern.compile("https?://\\S+");
        Matcher matcher = pattern.matcher(content);
        int count = 0;
        while (matcher.find()) {
            count++;
        }
        return count;
    }

    private int countEmojis(String content) {
        if (content == null) return 0;
        Pattern pattern = Pattern.compile("<a?:\\w+:\\d+>");
        Matcher matcher = pattern.matcher(content);
        int count = 0;
        while (matcher.find()) {
            count++;
        }
        return count;
    }
}