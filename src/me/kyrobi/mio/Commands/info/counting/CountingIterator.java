package me.kyrobi.mio.Commands.info.counting;

import me.kyrobi.mio.Main;
import me.kyrobi.mio.utils.Sqlite;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.restaction.pagination.MessagePaginationAction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*
    Reference: https://stackoverflow.com/questions/70525532/trying-to-iterate-through-all-messages-in-a-discord-channel-with-jda
 */

public class CountingIterator extends ListenerAdapter {

    @Override //Overrides the super class
    public void onGuildMessageReceived(GuildMessageReceivedEvent e){
        String[] args = e.getMessage().getContentRaw().split(" "); // split up every argument since every argument has a space

        if((args[0].equalsIgnoreCase(Main.prefix + "counting") && args[1].equalsIgnoreCase( "iterate")) && (e.getAuthor().getIdLong() == 559428414709301279L)){

            Sqlite sqlite = new Sqlite();

            e.getChannel().getIterableHistory().stream().limit(60000).forEach(
                    message -> {
                        System.out.println(message.getContentRaw());

                        //If the user is not in the database, we add them to it
                        if(sqlite.getCount(message.getAuthor().getIdLong()) == 0){
                            System.out.println("Creating new profile for user");
                            sqlite.insert(message.getAuthor().getIdLong(), 1, message.getAuthor().getAsTag());
                        }
                        //If user already exists, increment their data
                        else{
                            int currentAmount = sqlite.getAmount(message.getAuthor().getIdLong());
                            sqlite.update(message.getAuthor().getIdLong(), ++currentAmount, message.getAuthor().getAsTag());
                        }
                    }
            );





//            List<Message> messages = null;
//
//            for(int i = 0; i < 2; i++){
//                messages.add((Message) e.getChannel().getHistory().retrievePast(100).complete());
//            }
//
//            System.out.println("There are " + messages.size() + " messages in list.");
//            for(int i = 0; i < messages.size(); i++){
//                System.out.println(messages.get(i).getContentRaw());
//            }
//
//            System.out.println("Something...");
        }
    }
}
