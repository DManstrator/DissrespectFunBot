package tk.dmanstrator.discordbots.commands.impl;

import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import tk.dmanstrator.discordbots.commands.ICommand;
import tk.dmanstrator.discordbots.utils.DiscordUtils;

public class Bruder implements ICommand  {

    @Override
    public String getName() {
        return "Bruder";
    }

    @Override
    public String getHelp() {
        return "Muss los!";
    }

    @Override
    public void execute(String[] args, MessageReceivedEvent event) {
        MessageChannel channel = DiscordUtils.getCorrectMessageChannel(event);
        channel.sendMessage("https://giphy.com/gifs/vbt-mro-vs-peat-SSW5Adq4ujubskxKfJ").queue();
    }
}