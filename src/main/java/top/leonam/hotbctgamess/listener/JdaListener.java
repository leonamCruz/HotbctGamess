package top.leonam.hotbctgamess.listener;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.stereotype.Component;
import top.leonam.hotbctgamess.interfaces.Command;
import top.leonam.hotbctgamess.registers.CommandRegistry;
import top.leonam.hotbctgamess.service.MessageService;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@AllArgsConstructor
@Slf4j
public class JdaListener extends ListenerAdapter {

    private final CommandRegistry registry;
    private final MessageService messageService;
    private static final Pattern URL_PATTERN = Pattern.compile("https?://\\S+");

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;

        registerMessage(event);

        processCommand(event);
    }

    @Override
    public void onMessageUpdate(MessageUpdateEvent event) {
        if (event.getAuthor() == null || event.getAuthor().isBot()) return;

        messageService.updateMessage(
                event.getMessageIdLong(),
                event.getMessage().getContentRaw()
        );
    }

    @Override
    public void onMessageDelete(MessageDeleteEvent event) {
        messageService.markAsDeleted(event.getMessageIdLong());
    }

    private void registerMessage(MessageReceivedEvent event) {
        var msg = event.getMessage();
        String contentRaw = msg.getContentRaw();

        int mentionCount = msg.getMentions().getUsers().size()
                + msg.getMentions().getRoles().size()
                + msg.getMentions().getChannels().size();

        int linkCount = 0;
        Matcher matcher = URL_PATTERN.matcher(contentRaw);
        while (matcher.find()) {
            linkCount++;
        }

        int customEmojiCount = msg.getMentions().getCustomEmojis().size();

        messageService.saveMessage(
                msg.getIdLong(),
                event.getAuthor().getIdLong(),
                event.getAuthor().getName(),
                event.getAuthor().getDiscriminator(),
                event.getAuthor().getEffectiveAvatarUrl(),
                event.getChannel().getIdLong(),
                event.getChannel().getName(),
                contentRaw,
                msg.getTimeCreated().toInstant(),
                mentionCount,
                linkCount,
                customEmojiCount,
                msg.getStickers().size(),
                msg.getAttachments().size()
        );
    }

    private void processCommand(MessageReceivedEvent event) {
        String raw = event.getMessage().getContentRaw();
        if (raw.isBlank()) return;

        String commandName = raw.split("\\s+")[0];
        Command command = registry.get(commandName);

        if (command != null) {
            EmbedBuilder message = command.execute(event);
            event.getMessage().replyEmbeds(message.build()).queue();
        }
    }
}