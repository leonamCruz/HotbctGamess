package top.leonam.hotbctgamess.service.commands;

import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.stereotype.Service;
import top.leonam.hotbctgamess.interfaces.Command;
import top.leonam.hotbctgamess.service.StatsService;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class Status implements Command {
    private final StatsService statsService;

    @Override
    public String name() {
        return "~mineração";
    }

    @Override
    public EmbedBuilder execute(MessageReceivedEvent event) {
        return statsService.getDailyStatsEmbed(LocalDate.now());
    }
}
