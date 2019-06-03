package tk.dmanstrator.discordbots.commands.impl;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.SelfUser;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import tk.dmanstrator.discordbots.DissrespectFunBot;
import tk.dmanstrator.discordbots.commands.ICommand;
import tk.dmanstrator.discordbots.utils.DiscordUtils;
import tk.dmanstrator.discordbots.utils.DissrespectUtils;

public class Help implements ICommand  {
    
    // Can't store Maps since they are not available while instancing this Object
    private DissrespectFunBot disscordFunBot;
    
    // for later access
    private Map<String, ICommand> userOnlyCommands;
    private Map<String, ICommand> allCommands;

    public Help(DissrespectFunBot disscordFunBot) {
        this.disscordFunBot = disscordFunBot;
    }

    @Override
    public String getName() {
        return "Help";
    }

    @Override
    public String getHelp() {
        return "Prints help for the Bot";
    }

    @Override
    public void execute(String[] args, MessageReceivedEvent event) {
        MessageChannel channel = DiscordUtils.getCorrectMessageChannel(event);
        
        Map<String, ICommand> userCommands = disscordFunBot.getUserCommands();
        Map<String, ICommand> adminCommands = disscordFunBot.getAdminCommands();
        
        if (userOnlyCommands == null)  {
            userOnlyCommands = new HashMap<>(userCommands);
        }
        if (allCommands == null)  {
            allCommands = new HashMap<>(userCommands);
            allCommands.putAll(adminCommands);
        }
        
        boolean admin;
        if (event.isFromType(ChannelType.PRIVATE))  {
            admin = DissrespectUtils.isAdmin(event.getAuthor());
        }  else  {
            admin = DissrespectUtils.isAdmin(event.getGuild(), event.getMember());
        }
        
        MessageEmbed embed;
        if (admin)  {
            embed = getHelpEmbed(allCommands, event);
        }  else  {
            embed = getHelpEmbed(userOnlyCommands, event);
        }
        
        channel.sendMessage(embed).queue();
    }
    
    private MessageEmbed getHelpEmbed(Map<String, ICommand> commands, MessageReceivedEvent event)  {
        SelfUser selfUser = event.getJDA().getSelfUser();
        String botName = selfUser.getName();
        String botAvatarUrl = selfUser.getAvatarUrl();
        
        User author = event.getAuthor();
        Member member = event.getMember();
        Color color = member != null ? member.getColor() : new Color(187, 32, 48);
        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setAuthor(botName, null, botAvatarUrl)
                .setDescription("You can use those commands for the Bot:")
                .setColor(color)
                .setFooter(String.format("Help requested by %s",
                        author.getAsTag()), author.getEffectiveAvatarUrl())
                .setTimestamp(event.getMessage().getCreationTime());
        
        for (Map.Entry<String, ICommand> entry : commands.entrySet())  {
            ICommand cmd = entry.getValue();
            embedBuilder.addField(String.format("%s%s (%s)", disscordFunBot.getPrefix(), entry.getKey(), cmd.getName()),
                    cmd.getHelp(), true);  // TODO Pagination
        }
        
        return embedBuilder.build();
        
    }

}