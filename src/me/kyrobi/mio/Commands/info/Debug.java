package me.kyrobi.mio.Commands.info;

import me.kyrobi.mio.Main;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Debug extends ListenerAdapter {

    @Override //Overrides the super class
    public void onGuildMessageReceived(GuildMessageReceivedEvent e){
//        String[] args = e.getMessage().getContentRaw().split(" "); // split up every argument since every argument has a space
//
//        System.out.println("Author: " + e.getAuthor().getIdLong());
//
//        if(e.getAuthor().getIdLong() == 559428414709301279L){
//            System.out.println("true");
//        }
//        else{
//            System.out.println("false");
//        }
//
//        if(args[0].equalsIgnoreCase(Main.prefix + "test")){
//            System.out.println("works");
//            e.getChannel().sendMessage("It works").queue();
//        }
        return;
    }
}
