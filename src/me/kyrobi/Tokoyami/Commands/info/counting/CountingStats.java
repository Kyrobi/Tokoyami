package me.kyrobi.Tokoyami.Commands.info.counting;

import me.kyrobi.Tokoyami.Main;
import me.kyrobi.Tokoyami.utils.Sqlite;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class CountingStats extends ListenerAdapter {

    @Override //Overrides the super class
    public void onGuildMessageReceived(GuildMessageReceivedEvent e){
        String[] args = e.getMessage().getContentRaw().split(" "); // split up every argument since every argument has a space

        if((args[0].equalsIgnoreCase(Main.prefix + "cstats"))) {
            Sqlite sqlite = new Sqlite();
            e.getChannel().sendMessage(e.getMessage().getAuthor().getAsMention() + "'s counting contributions: " + sqlite.getAmount(e.getAuthor().getIdLong())).queue();
        }
    }
}
