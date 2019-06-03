package tk.dmanstrator.discordbots.utils;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class DiscordUtils {
    
    private DiscordUtils()  {} // prohibit instancing
    
    private static JDA jda;
    private final static Long mainGuildId = 353921999401385986L;
    private final static Long errorChannel = 485249676224888854L;
    
    public static void setJDA(JDA jda)  {
        DiscordUtils.jda = jda;
    }
    
    private static boolean isJDAAvaliable()  {
        return jda != null;
    }

    public static void sendCrashReport(Throwable ex)  {
        if (!isJDAAvaliable())  {
            System.err.println("JDA not avaliable!");
            return;
        }
        ex.printStackTrace();
        
        List<StackTraceElement> asList = Arrays.asList(ex.getStackTrace());
        String stacktrace = asList.stream().map(entry -> "\t\t " + entry.toString()).collect(Collectors.joining("\n"));
        
        StringBuilder builder = new StringBuilder("```\n");
        builder.append(String.format("%s: %s\n", ex.getClass().getName(), ex.getMessage()));
        builder.append(stacktrace);
        builder.append("\n```");
        String error = builder.toString();
        
        if (error.length() > 2000)  {
            try {
                error.replace("```\n", "").replaceAll("\n```", "");
                error = String.format("A very long exception occured! Link: %s.txt", HasteUtils.paste(error));
            } catch (IOException e) {
                sendCrashReport(e);
            }
        }
        
        jda.getGuildById(mainGuildId).getTextChannelById(errorChannel).sendMessage(error).queue();
    }
    
    public static String textWithMention(User user, String format, Object... args)  {
        return String.format("%s %s", user.getAsMention(), String.format(format, args));
    }

    public static MessageChannel getCorrectMessageChannel(MessageReceivedEvent event) {
        MessageChannel channel;
        if (event.isFromType(ChannelType.PRIVATE))  {
            channel = event.getPrivateChannel();
        }  else  {
            channel = event.getTextChannel();
        }
        return channel;
    }
}