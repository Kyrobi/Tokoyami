package me.kyrobi.mio.Auto;

import me.kyrobi.mio.Main;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class AnnouncementReaction extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e){
        if(e.getChannel().getIdLong() == Main.announcementChannelID){
            // e.getMessage().addReaction("AyameF:917176926924337262").queue(); //Use this to send custom emotes
            e.getMessage().addReaction("\uD83D\uDC4D").queue();
            e.getMessage().addReaction("\uD83D\uDC4E").queue();
        }
    }
}
