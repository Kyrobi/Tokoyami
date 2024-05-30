package me.kyrobi.Tokoyami.Auto;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.CompletableFuture;

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
            event.getMessage().getAttachments().forEach(attachment -> {
                CompletableFuture.runAsync(() -> {
                    try {
                        // Download the attachment
                        URL url = new URL(attachment.getUrl());
                        try (InputStream in = url.openStream()) {
                            Path tempFile = Files.createTempFile("attachment", attachment.getFileExtension());
                            Files.copy(in, tempFile, StandardCopyOption.REPLACE_EXISTING);

                            // Reupload the file
                            destinationChannel.sendMessage("**" + channelName + " | " + senderName + "**:")
                                    .addFiles(net.dv8tion.jda.api.utils.FileUpload.fromData(tempFile.toFile(), attachment.getFileName()))
                                    .queue();

                            // Delete the temporary file
                            Files.delete(tempFile);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            });
        }

        // Check if the message has content (excluding attachments)
        if (!content.isEmpty()) {
            // Forward the message content to the destination channel
            destinationChannel.sendMessage("**" + channelName + " | "+ senderName + "**: `" + content + "`").queue();
        }

        // Check if the message contains stickers
        event.getMessage().getStickers().forEach(sticker -> {
            CompletableFuture.runAsync(() -> {
                try {
                    URL url = new URL(sticker.getIconUrl());
                    try (InputStream in = url.openStream()) {
                        Path tempFile = Files.createTempFile("sticker", ".png");
                        Files.copy(in, tempFile, StandardCopyOption.REPLACE_EXISTING);

                        destinationChannel.sendMessage("**" + channelName + " | " + senderName + "**:")
                                .addFiles(net.dv8tion.jda.api.utils.FileUpload.fromData(tempFile.toFile(), sticker.getName() + ".png"))
                                .queue();

                        Files.delete(tempFile);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        });

        // Check if the message contains emojis
        event.getMessage().getMentions().getCustomEmojis().forEach(emoji -> {
            CompletableFuture.runAsync(() -> {
                try {
                    URL url = new URL(emoji.getImageUrl());
                    try (InputStream in = url.openStream()) {
                        Path tempFile = Files.createTempFile("emoji", ".png");
                        Files.copy(in, tempFile, StandardCopyOption.REPLACE_EXISTING);

                        destinationChannel.sendMessage("**" + channelName + " | " + senderName + "**:")
                                .addFiles(net.dv8tion.jda.api.utils.FileUpload.fromData(tempFile.toFile(), emoji.getName() + ".png"))
                                .queue();

                        Files.delete(tempFile);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        });
    }
}
