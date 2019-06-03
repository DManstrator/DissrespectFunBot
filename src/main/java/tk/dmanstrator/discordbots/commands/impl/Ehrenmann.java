package tk.dmanstrator.discordbots.commands.impl;

import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import tk.dmanstrator.discordbots.commands.ICommand;
import tk.dmanstrator.discordbots.utils.DiscordUtils;

public class Ehrenmann implements ICommand  {

    @Override
    public String getName() {
        return "Ehrenmann";
    }

    @Override
    public String getHelp() {
        return "mRo ist das einfach, keine Ausreden!";
    }

    @Override
    public void execute(String[] args, MessageReceivedEvent event) {
        MessageChannel channel = DiscordUtils.getCorrectMessageChannel(event);
        channel.sendMessage("https://cdn.discordapp.com/attachments/584884556612501514/584896140512722954/unknown.png").queue();
    }
}