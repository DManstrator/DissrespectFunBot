package tk.dmanstrator.discordbots.eventhandler;

import java.util.Arrays;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class CommandParser {
    
    private final String prefix;
    
    public CommandParser(String prefix) {
        this.prefix = prefix;
    }

    public CommandContainer parse(String content, MessageReceivedEvent e)  {
        String contentWithoutPrefix = content.replaceFirst(prefix, "");
        
        int firstSpaceIndex = contentWithoutPrefix.indexOf(' ');
        // no arguments? Don't split, useless.
        if (firstSpaceIndex == -1)  {
            return new CommandContainer(content, contentWithoutPrefix, new String[0], e);
        }
        
        String command = contentWithoutPrefix.substring(0, firstSpaceIndex);
        String[] split = contentWithoutPrefix.split("\\s+(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
        String[] args = Arrays.asList(split).stream()
                .skip(1)  // ignore command
                .map(entry -> entry.replace("\"", ""))
                .toArray(String[]::new);

        return new CommandContainer(content, command, args, e);
    }
    
    public class CommandContainer  {
        public final String raw;
        public final String command;
        public final String[] args;
        public final MessageReceivedEvent event;
        
        public CommandContainer(String raw, String command, String[] args, MessageReceivedEvent event)  {
            this.raw = raw;
            this.command = command;
            this.args = args;
            this.event = event;
        }
    }
    
}