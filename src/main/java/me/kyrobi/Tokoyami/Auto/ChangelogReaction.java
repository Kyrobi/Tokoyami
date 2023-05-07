package me.kyrobi.Tokoyami.Auto;

import me.kyrobi.Tokoyami.Main;
import net.dv8tion.jda.api.entities.emoji.CustomEmoji;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ChangelogReaction extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent e){
        if(e.getChannel().getIdLong() == Main.ChangelogChannelID){
            // e.getMessage().addReaction("AyameF:917176926924337262").queue(); //Use this to send custom emotes
//            CustomEmoji thumbsUp = e.getJDA().getEmojiById("588622628751802382");
//            CustomEmoji thumbsDown = e.getJDA().getEmojiById("588622628751802382");
//            Emoji thumbsUp = e.getJDA().getEmojiById("\uD83D\uDC4D");
//            Emoji thumbsDown = e.getJDA().getEmojiById("\uD83D\uDC4E");

            e.getMessage().addReaction(Emoji.fromUnicode("U+1F44D")).queue();
            e.getMessage().addReaction(Emoji.fromUnicode("U+1F44E")).queue();
        }
    }
}
