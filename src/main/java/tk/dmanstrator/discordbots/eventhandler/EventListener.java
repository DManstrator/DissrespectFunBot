package tk.dmanstrator.discordbots.eventhandler;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import tk.dmanstrator.discordbots.DissrespectFunBot;
import tk.dmanstrator.discordbots.commands.ICommand;
import tk.dmanstrator.discordbots.utils.DiscordUtils;
import tk.dmanstrator.discordbots.utils.DissrespectUtils;

public class EventListener extends ListenerAdapter {
    
    private final String prefix;
    private final Map<String, ICommand> userCmds;
    private final Map<String, ICommand> adminCmds;
    private final List<ICommand> noPrefixes;
    
    private final CommandParser parser;
    
    public EventListener(DissrespectFunBot disscordFunBot) {
        prefix = disscordFunBot.getPrefix();
        userCmds = disscordFunBot.getUserCommands();
        adminCmds = disscordFunBot.getAdminCommands();
        noPrefixes = disscordFunBot.getNoPrefixes();
        
        parser = new CommandParser(prefix);
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot())  {
            return;
        }
        
        for (ICommand cmd : noPrefixes)  {
            cmd.execute(null, event);
        }
        
        String contentRaw = event.getMessage().getContentRaw();
        if (contentRaw.startsWith(prefix))  {
            try  {
                handleCommand(parser.parse(event.getMessage().getContentRaw(), event));
            } catch (Exception e)  {  // when something goes wrong, log it.
                DiscordUtils.sendCrashReport(e);
            }
        }
    }
    
    public void handleCommand(CommandParser.CommandContainer cmd)  {
        ICommand iCommand = null;
        
        String cmdStr = cmd.command.toLowerCase();
        if (userCmds.containsKey(cmdStr))  {
            iCommand = userCmds.get(cmdStr);
        }
        else if (adminCmds.containsKey(cmdStr))  {
            iCommand = adminCmds.get(cmdStr);
        }
        
        if (iCommand == null)  {
            System.err.println(String.format("Command '%s' not found!", cmdStr));
            return;
        }
        
        String[] args = cmd.args;
        MessageReceivedEvent event = cmd.event;

        boolean admin;
        Member member;
        User author = event.getAuthor();
        MessageChannel msgChan = DiscordUtils.getCorrectMessageChannel(event);
        
        if (event.isFromType(ChannelType.PRIVATE))  {
            member = author.getMutualGuilds().stream().map(guild -> guild.getMember(author)).findFirst().orElse(null);
            admin = DissrespectUtils.isAdmin(author);
        }  else  {
            Guild guild = event.getGuild();
            TextChannel textChannel = event.getTextChannel();
            Collection<Permission> neededPermissions = iCommand.getNeededPermissions();
            
            member = event.getMember();
            admin = DissrespectUtils.isAdmin(guild, member);
            
            if (!guild.getSelfMember().hasPermission(textChannel, neededPermissions))  {
                String perms = neededPermissions.stream().map(perm -> perm.getName()).collect(Collectors.joining(", "));
                String info = DiscordUtils.textWithMention(author, "I need the following permissions to execute that command: %s", perms);
                msgChan.sendMessage(info).queue();
                return;
            }
        }
        
        if (adminCmds.containsKey(cmdStr) && !admin)  {
            msgChan.sendMessage(DiscordUtils.textWithMention(author, "You don't have the rights to perform that action!")).queue();
            return;
        }
        
        if (member != null)  {
            boolean offline = member.getOnlineStatus().equals(OnlineStatus.OFFLINE);
            if (offline)  {
                msgChan.sendMessage(DiscordUtils.textWithMention(author, "How can you write something when you are offline? \uD83D\uDC40")).queue();
                return;
            }
        }
        
        iCommand.execute(args, event);
    }
    
}