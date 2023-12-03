package me.kyrobi.Tokoyami.Auto;

import me.kyrobi.Tokoyami.Main;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.Route;

import static me.kyrobi.Tokoyami.Main.jda;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RemoveLinks extends ListenerAdapter {

    ArrayList<Role> whiteListedRoles = new ArrayList<Role>();
    private ArrayList<Pattern> filteredChat = new ArrayList<>();
    private ArrayList<String> whitelistedWords = new ArrayList<>();

    public RemoveLinks(JDA jda){

        Guild guild = jda.getGuildById("415873891857203212");

        filteredChat.add(Pattern.compile(
                "[a-zA-Z0-9\\-\\.\\*]+\\s?(\\.|\\*|dot|\\(dot\\)|-|\\(\\*\\)|;|:|,)\\s?(c(| +)o(| +)m|o(| +)r(| +)g|n(| +)e(| +)t|(?<! )c(| +)z|(?<! )c(| +)o|(?<! )u(| +)k|(?<! )s(| +)k|b(| +)i(| +)z|(?<! )m(| +)o(| +)b(| +)i|(?<! )x(| +)x(| +)x|(?<! )e(| +)u|(?<! )m(| +)e|(?<! )i(| +)o|(?<! )o(| +)n(| +)l(| +)i(| +)n(| +)e|(?<! )x(| +)y(| +)z|(?<! )f(| +)r|(?<! )b(| +)e|(?<! )d(| +)e|(?<! )c(| +)a|(?<! )a(| +)l|(?<! )a(| +)i|(?<! )d(| +)e(| +)v|(?<! )a(| +)p(| +)p|(?<! )i(| +)n|(?<! )i(| +)s|(?<! )g(| +)g|(?<! )t(| +)o|(?<! )p(| +)h|(?<! )n(| +)l|(?<! )i(| +)d|(?<! )i(| +)n(| +)c|(?<! )u(| +)s|(?<! )p(| +)w|(?<! )p(| +)r(| +)o|(?<! )t(| +)v|(?<! )c(| +)x|(?<! )m(| +)x|(?<! )f(| +)m|(?<! )c(| +)c|(?<! )v(| +)i(| +)p|(?<! )f(| +)u(| +)n|(?<! )i(| +)c(| +)u)\\b"
                , Pattern.CASE_INSENSITIVE));

        whiteListedRoles.add( guild.getRoleById(469284766240210944L) ); // VIP
        whiteListedRoles.add( guild.getRoleById(750227892822212759L) ); // Level 10
        whiteListedRoles.add( guild.getRoleById(750228204832292885L) ); // Level 20
        whiteListedRoles.add( guild.getRoleById(750229923100229693L) ); // Level 30
        whiteListedRoles.add( guild.getRoleById(750228369924423730L) ); // Level 40
        whiteListedRoles.add( guild.getRoleById(750228746161618955L) ); // Level 50
        whiteListedRoles.add( guild.getRoleById(750228484072407060L) ); // Level 60
        whiteListedRoles.add( guild.getRoleById(750230251811897385L) ); // Level 70
        whiteListedRoles.add( guild.getRoleById(750229654803185725L) ); // Level 80
        whiteListedRoles.add( guild.getRoleById(750230843062222888L) ); // Level 85
        whiteListedRoles.add( guild.getRoleById(750230496075579444L) ); // Level 90
        whiteListedRoles.add( guild.getRoleById(750231302711672832L) ); // Level 95
        whiteListedRoles.add( guild.getRoleById(750231871744376833L) ); // Level 100

        whitelistedWords.add("https://discordapp.com/invite/B5JW7qp");
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent e){

        // Do nothing if bot
        if(e.getAuthor().isBot()){ return; }

        Member member = e.getMember();
        // Do nothing is user has hte proper roles
        if(hasProperRole(member)){ return; }

        Message message = e.getMessage();

        if(isURL(message.getContentRaw())){
            int timeOutInMinutes = 5;
            message.delete().queueAfter(200, TimeUnit.MILLISECONDS); // Delete the message
            TextChannel channel = message.getChannel().asTextChannel();
            channel.sendMessage(member.getAsMention() + " You need to have either:\n" +
                    "1. VIP role or\n" +
                    "2. Level 10 from chatting\n" +
                    "to be able to send links\n\n" +
                    "You've been temporarily timed out for " + timeOutInMinutes + " minutes."
            ).queue();
            member.timeoutFor(timeOutInMinutes, TimeUnit.MINUTES).queue();
        }
    }

    private boolean hasProperRole(Member member){
        List<Role> memberRoles = member.getRoles();

        for(Role role: whiteListedRoles){
            if(memberRoles.contains(role)){
                return true;
            }
        }

        return false;
    }

    private boolean isURL(String message){
        for(String string: message.split(" ")){
            if(whitelistedWords.contains(string)){
                message = message.replace(string, "");
            }
        }

        for(Pattern pattern: filteredChat){
            Matcher matcher = pattern.matcher(message);
            if(matcher.find()){
                return true;
            }
        }

        return false;
    }
}
