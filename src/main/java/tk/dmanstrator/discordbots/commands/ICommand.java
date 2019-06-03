package tk.dmanstrator.discordbots.commands;

import java.util.Arrays;
import java.util.Collection;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public interface ICommand {
    String getName();
    String getHelp();
    default Collection<Permission> getNeededPermissions() {
        return Arrays.asList();
    }
    void execute(String[] args, MessageReceivedEvent event);
    
}