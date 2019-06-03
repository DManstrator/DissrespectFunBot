package tk.dmanstrator.discordbots.commands.impl.auto;

import java.util.Arrays;
import java.util.List;

import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import tk.dmanstrator.discordbots.commands.ICommand;
import tk.dmanstrator.discordbots.utils.DiscordUtils;

public class Spass implements ICommand  {
    
    List<String> trigger = Arrays.asList("spaß", "spass");

    @Override
    public String getName() {
        return "Spaß!";
    }

    @Override
    public String getHelp() {
        return "mRohstoff ist ein Ehrenmann";
    }

    @Override
    public void execute(String[] args, MessageReceivedEvent event) {
        String content = event.getMessage().getContentStripped().trim().toLowerCase();
        boolean triggered = trigger.stream().anyMatch(elem -> content.startsWith(elem));
        if (trigger.contains(content) || triggered)  {
            MessageChannel channel = DiscordUtils.getCorrectMessageChannel(event);
            channel.sendMessage("https://cdn.discordapp.com/attachments/573272860294905886/578659139178463242/unknown.png").queue();
        }
        
    }

}