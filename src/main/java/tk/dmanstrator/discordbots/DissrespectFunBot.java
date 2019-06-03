package tk.dmanstrator.discordbots;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import tk.dmanstrator.discordbots.commands.ICommand;
import tk.dmanstrator.discordbots.commands.impl.Bruder;
import tk.dmanstrator.discordbots.commands.impl.Ehrenmann;
import tk.dmanstrator.discordbots.commands.impl.EventWaiterTest;
import tk.dmanstrator.discordbots.commands.impl.Help;
import tk.dmanstrator.discordbots.commands.impl.auto.AutoEhrenmann;
import tk.dmanstrator.discordbots.commands.impl.auto.Spass;
import tk.dmanstrator.discordbots.eventhandler.EventListener;
import tk.dmanstrator.discordbots.utils.Configuration;
import tk.dmanstrator.discordbots.utils.DiscordUtils;
import tk.dmanstrator.discordbots.utils.EventWaiter;

public class DissrespectFunBot {
    
    private final JDA jda;
    private final String prefix;
    private final EventWaiter waiter;
    private final List<ICommand> noPrefixes;
    private final Map<String, ICommand> userCommands;
    private final Map<String, ICommand> adminCommands;
    
    public DissrespectFunBot()  {
        prefix = "diss!";
        waiter = new EventWaiter();
        noPrefixes = initNoPrefixes();
        userCommands = initUserCommands();
        adminCommands = initAdminCommands();
        
        Configuration config = new Configuration("config.json");
        
        JDA tmpJda = null;
        try {
            tmpJda = new JDABuilder(config.getToken())
                    .addEventListener(waiter)
                    .addEventListener(new EventListener(this))
                    .build().awaitReady();
            DiscordUtils.setJDA(tmpJda);
        } catch (LoginException e) {
            System.err.println("Could not start the Bot, invalid Token!");
            System.exit(-1);
        } catch (InterruptedException e) {
            System.err.println("Could not start the Bot, interrupred while waiting!");
            System.exit(-1);
        }
        jda = tmpJda;
    }
    
    private Map<String, ICommand> initUserCommands() {
        Map<String, ICommand> cmds = new HashMap<>();
        cmds.put("help", new Help(this));
        cmds.put("test", new EventWaiterTest(waiter));
        cmds.put("bruder", new Bruder());
        cmds.put("ehrenmann", new Ehrenmann());
        return cmds;
    }

    private Map<String, ICommand> initAdminCommands() {
        Map<String, ICommand> cmds = new HashMap<>();
        return cmds;
    }
    
    private List<ICommand> initNoPrefixes() {
        List<ICommand> list = new ArrayList<>();
        list.add(new Spass());
        list.add(new AutoEhrenmann());
        return list;
    }
    
    public JDA getJDA() {
        return jda;
    }

    public EventWaiter getWaiter() {
        return waiter;
    }

    public String getPrefix() {
        return prefix;
    }

    public Map<String, ICommand> getUserCommands() {
        return userCommands;
    }

    public Map<String, ICommand> getAdminCommands() {
        return adminCommands;
    }
    
    public List<ICommand> getNoPrefixes() {
        return noPrefixes;
    }

    public static void main(String[] args) {
        new DissrespectFunBot();
    }

}