package me.kyrobi.mio.Commands.fun;

import me.kyrobi.mio.Main;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Mitty extends ListenerAdapter {

    @Override //Overrides the super class
    public void onGuildMessageReceived(GuildMessageReceivedEvent e){
        String[] args = e.getMessage().getContentRaw().split(" "); // split up every argument since every argument has a space

        if(args[0].equalsIgnoreCase("mitty")){
            e.getChannel().sendMessage("https://media.discordapp.net/attachments/734634926384742440/756767545473892432/MITTY.png?width=400&height=225").queue();
        }
    }
}
