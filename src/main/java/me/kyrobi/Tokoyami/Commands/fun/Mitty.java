package me.kyrobi.Tokoyami.Commands.fun;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Mitty extends ListenerAdapter {

    @Override //Overrides the super class
    public void onMessageReceived(MessageReceivedEvent e){
        String[] args = e.getMessage().getContentRaw().split(" "); // split up every argument since every argument has a space

        if(args[0].equalsIgnoreCase("mitty")){
            e.getChannel().sendMessage("https://media.discordapp.net/attachments/734634926384742440/756767545473892432/MITTY.png?width=400&height=225").queue();
        }

        if(args[0].equalsIgnoreCase("confused")){
            e.getChannel().sendMessage("https://cdn.discordapp.com/attachments/845435268974116864/934996764921057300/fubuki-confused.gif").queue();
        }
    }
}
