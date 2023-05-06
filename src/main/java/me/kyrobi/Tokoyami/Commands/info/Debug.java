package me.kyrobi.Tokoyami.Commands.info;

import me.kyrobi.Tokoyami.Main;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Debug extends ListenerAdapter {

    @Override //Overrides the super class
    public void onMessageReceived(MessageReceivedEvent e){

        String[] args = e.getMessage().getContentRaw().split(" "); // split up every argument since every argument has a space

        if(!args[0].equalsIgnoreCase(Main.prefix + "t")){
            return;
        }

//        FieldObject suggestionTemplate = new FieldObject();
//
//        e.getChannel().sendMessage(suggestionTemplate.getDisclaimer()).queue();
//
//        User user = e.getAuthor();
//
//        /*
//        Sends the message to user's DM
//         */
//        user.openPrivateChannel().queue((channel) ->
//        {
//            channel.sendMessage(suggestionTemplate.getDisclaimer()).queue();
//        });

   }
}
