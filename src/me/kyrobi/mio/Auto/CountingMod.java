package me.kyrobi.mio.Auto;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.List;

public class CountingMod extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e){
        if(e.getChannel().getIdLong() == 799121727250235392L){
            System.out.println("A message was sent in the counting channel");

            List<Message> messages = e.getChannel().getHistory().retrievePast(2).complete(); //Gets the last two message and store them to the list

            int sent;
            int previous;
            int expectedValue;

            //Try to convert the message to an int to do math
            try{
                sent = Integer.parseInt(e.getMessage().getContentRaw());
                previous = Integer.parseInt(messages.get(1).getContentRaw());
            }

            catch(NumberFormatException error){
                System.out.println("Deleted " + e.getMessage().getContentRaw() + " from counting" + " sent by " + e.getMember().getNickname() + " | " + e.getMember() + "\n");
                e.getMessage().delete().queue(); //Assume that if it can't convert, the message isn't right because it's not an int in the first place
                return;
            }

            expectedValue = previous + 1;
            if(expectedValue != sent){
                System.out.println("Deleted " + e.getMessage().getContentRaw() + " from counting" + " sent by " + e.getMember().getNickname() + " | " + e.getMember());
                System.out.println("Previous: " + previous + ". Expecting: " + expectedValue + ". But user sent " + sent + "\n");
                e.getMessage().delete().queue();
            }
        }
    }
}
