package me.kyrobi.Tokoyami.Auto;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class PictureChannelModeration extends ListenerAdapter {

    private static final long PICTURE_CATEGORY_ID = 1259911077152555088L;

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;
        if (!(event.getChannel() instanceof TextChannel)) return;
        TextChannel channel = (TextChannel) event.getChannel();
        String parentId = channel.getParentCategoryId();
        if (parentId == null || !parentId.equals(String.valueOf(PICTURE_CATEGORY_ID))) return;
        if (event.getMessage().getAttachments().isEmpty()) {
            event.getMessage().delete().queue();
        }
    }
}