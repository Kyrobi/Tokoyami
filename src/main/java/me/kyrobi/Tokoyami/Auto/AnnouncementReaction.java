package me.kyrobi.Tokoyami.Auto;

import me.kyrobi.Tokoyami.Main;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.emoji.CustomEmoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import static me.kyrobi.Tokoyami.Main.jda;

public class AnnouncementReaction extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent e){
        if(e.getChannel().getIdLong() == Main.announcementChannelID){
            // e.getMessage().addReaction("AyameF:917176926924337262").queue(); //Use this to send custom emotes

            CustomEmoji thumbsUp = e.getJDA().getEmojiById("588622628751802382");
            CustomEmoji thumbsDown = e.getJDA().getEmojiById("588622628751802382");

            e.getMessage().addReaction(thumbsUp).queue();
            e.getMessage().addReaction(thumbsDown).queue();
        }

        // Ping the staffs in player-report channel
        if(e.getChannel().getIdLong() == 1097255545443979346L){
            if(e.getAuthor().getIdLong() == 562798054244220962L){
                TextChannel textChannel = jda.getTextChannelById(1097255545443979346L);
                textChannel.sendMessage("<@&456546886526959617>").queue();
            }
        }
    }
}
