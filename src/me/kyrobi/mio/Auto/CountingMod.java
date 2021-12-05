package me.kyrobi.mio.Auto;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.RestAction;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class CountingMod extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e){
        if(e.getChannel().getIdLong() == 799121727250235392L){
            System.out.println("A message was sent in the counting channel");
            System.out.println("Sent: " + e.getMessage().getContentRaw());

            //Gets the last two message and store them to the list
            List<Message> messages = e.getChannel().getHistory().retrievePast(2).complete();

            //Access the "2nd" element in the list because the first element is the message a user just sent
            String prevMessage = messages.get(1).getContentRaw();
            int sent;
            int previous;

            //Try to convert the message to an int to do math
            try{
                sent = Integer.parseInt(e.getMessage().getContentRaw());
                previous = Integer.parseInt(messages.get(1).getContentRaw());
            }

            catch(NumberFormatException e){

            }

//            if(e.getMessage().getContentRaw().equals("Haha"){
//
//            }
        }
    }
}
