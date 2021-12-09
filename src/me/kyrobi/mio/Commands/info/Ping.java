package me.kyrobi.mio.Commands.info;

import me.kyrobi.mio.Main;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

//https://github.com/zekroTJA/DiscordBot/blob/master/src/main/java/commands/essentials/Ping.java
public class Ping extends ListenerAdapter {

    @Override //Overrides the super class
    public void onGuildMessageReceived(GuildMessageReceivedEvent e){
        String[] args = e.getMessage().getContentRaw().split(" "); // split up every argument since every argument has a space

        if(args[0].equalsIgnoreCase(Main.prefix + "ping")){
            e.getChannel().sendMessage(e.getJDA().getGatewayPing() + "ms").queue();
        }
    }
}
