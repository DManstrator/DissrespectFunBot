package tk.dmanstrator.discordbots.commands.impl;

import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import tk.dmanstrator.discordbots.commands.ICommand;
import tk.dmanstrator.discordbots.utils.DiscordUtils;
import tk.dmanstrator.discordbots.utils.EventWaiter;

public class EventWaiterTest implements ICommand  {
    
    private final EventWaiter waiter;
    
    public EventWaiterTest(EventWaiter waiter) {
        this.waiter = waiter;
    }

    @Override
    public String getName() {
        return "Waiter Test";
    }

    @Override
    public String getHelp() {
        return "Tests the Event Waiter";
    }

    @Override
    public void execute(String[] args, MessageReceivedEvent event) {
        MessageChannel channel = DiscordUtils.getCorrectMessageChannel(event);
        channel.sendMessage("What is your name?").queue();
        waiter.waitForEvent(MessageReceivedEvent.class, x -> x.getChannel().equals(channel) && x.getAuthor().equals(event.getAuthor()), e -> {
            String contentRaw = e.getMessage().getContentRaw();
            channel.sendMessage("Nice to meet you, " + contentRaw + "! But do you like that name?").queue();
            waiter.waitForEvent(MessageReceivedEvent.class, x -> x.getChannel().equals(channel) && x.getAuthor().equals(event.getAuthor()), e2 -> {
                channel.sendMessage("Actually I don't care. lol *SIKE*").queue();
            });
        });
    }

}