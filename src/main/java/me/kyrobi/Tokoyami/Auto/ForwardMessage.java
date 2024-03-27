package me.kyrobi.Tokoyami.Auto;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ForwardMessage extends ListenerAdapter {

    TextChannel destinationChannel;

    public ForwardMessage(JDA jda){
        Guild guild = jda.getGuildById("415873891857203212");
        destinationChannel = guild.getTextChannelById("1220499636477890652");
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event){

        // Don't log messages sent in the logging channel
        if(event.getChannel().getId().equals("1220499636477890652")){
            return;
        }

        // If bot detects message events from other servers, ignore it
        if(!(event.getGuild().getIdLong() == 415873891857203212L)){
            return;
        }

        // if (event.getAuthor().isBot()) return; // Ignore messages from bots

        MessageChannel channel = event.getChannel();
        String content = event.getMessage().getContentDisplay();

        String senderName = event.getAuthor().getName();
        String channelName = event.getChannel().getName();

        // Check if the message contains any attachments (e.g., pictures, videos)
        if (!event.getMessage().getAttachments().isEmpty()) {
            // Forward each attachment to the destination channel
            event.getMessage().getAttachments().forEach(attachment ->
                    destinationChannel.sendMessage("**" + channelName + " | "+ senderName + "**: " + attachment.getUrl()).queue());
        }

        // Check if the message has content (excluding attachments)
        if (!content.isEmpty()) {
            // Forward the message content to the destination channel
            destinationChannel.sendMessage("**" + channelName + " | "+ senderName + "**: `" + content + "`").queue();
        }

        // Check if the message contains stickers
        event.getMessage().getStickers().forEach(sticker ->
                destinationChannel.sendMessage("**" + channelName + " | "+ senderName + "**: " + sticker.getIconUrl()).queue());

        // Check if the message contains emojis
        event.getMessage().getMentions().getCustomEmojis().forEach(emoji ->
                destinationChannel.sendMessage("**" + channelName + " | "+ senderName + "**: " + emoji.getImageUrl()).queue());

//        event.getMessage().getCu.forEach(emoji ->
//                destinationChannel.sendMessage("Emoji from " + event.getAuthor().getName() + ": " + emoji.getAsMention()).queue());
    }
}
