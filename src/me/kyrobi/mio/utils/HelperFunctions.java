package me.kyrobi.mio.utils;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class HelperFunctions extends ListenerAdapter {

    public static void sendPrivateMessage(User user, String content){
        user.openPrivateChannel().queue((channel) ->
        {
            channel.sendMessage(content).queue();
        });
    }
}
