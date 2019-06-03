package tk.dmanstrator.discordbots.utils;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.User;

public class DissrespectUtils {
    
    private DissrespectUtils()  {} // prohibit instancing
    
    private final static Long serverId = 572834541832503316L;
    private final static Long staffRoleId = 573287529571287050L;
    
    /**
     * Checks if a Member has Admin State in a Guild.
     * @param guild Guild to check
     * @param member Member to check
     * @return true if member has Admin Rights
     */
    public static boolean isAdmin(Guild guild, Member member)  {
        boolean admin = member.hasPermission(Permission.ADMINISTRATOR);
        if (admin)  {
            return true;
        }  else  {            
            return member.getRoles().stream().anyMatch(role -> role.getIdLong() == staffRoleId);
        }
    }
    
    public static boolean isAdmin(User user)  {
        Guild guild = user.getJDA().getGuildById(serverId);
        Member member = guild.getMember(user);
        return isAdmin(guild, member);
    }

}
