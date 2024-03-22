package me.kyrobi.Tokoyami.Auto;

import me.kyrobi.Tokoyami.Main;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.emoji.CustomEmoji;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.concurrent.TimeUnit;

import static me.kyrobi.Tokoyami.Main.jda;

public class AnnouncementReaction extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent e){
        if(e.getChannel().getIdLong() == Main.announcementChannelID){
            // e.getMessage().addReaction("AyameF:917176926924337262").queue(); //Use this to send custom emotes

//            Emoji thumbsUp = e.getJDA().getEmojiById("588622628751802382");
//            Emoji thumbsDown = e.getJDA().getEmojiById("588622628751802382");

//            long thumbsUpId = 0x1f44dL;
//            long thumbsDownId = 0x1f44eL;

//            Emoji thumbsUp = e.getJDA().getEmojiById("U+1F44D");
//            Emoji thumbsDown = e.getJDA().getEmojiById("U+1F44E");

            e.getMessage().addReaction(Emoji.fromUnicode("U+1F44D")).queueAfter(1000, TimeUnit.MILLISECONDS);
            e.getMessage().addReaction(Emoji.fromUnicode("U+1F44E")).queueAfter(1000, TimeUnit.MILLISECONDS);
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
