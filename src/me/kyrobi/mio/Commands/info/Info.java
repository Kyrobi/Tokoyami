package me.kyrobi.mio.Commands.info;

import me.kyrobi.mio.Main;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Info extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e){
        String[] args = e.getMessage().getContentRaw().split(" ");

        if((args[0].equalsIgnoreCase(Main.prefix + "info")) || (args[0].equalsIgnoreCase(Main.prefix + "help"))){

            StringBuilder stringBuilder = new StringBuilder();  // For appending strings read from an external file
            String help_message = null;
            // Read the info message from a txt file
            try {
                FileReader reader = new FileReader("data/info.txt");

                // The reader returns an int value representing each character in the file. Returns -1 when end of file
                int data = reader.read();
                while(data != -1){
                    stringBuilder.append(String.valueOf((char)data));
                    data = reader.read();
                }
                reader.close();
            }

            catch (IOException ex) {
                ex.printStackTrace();
            }

            help_message = stringBuilder.toString();

            try{
                e.getChannel().sendMessage(help_message).queue();
            }
            catch(IllegalArgumentException message_empty){
                e.getChannel().sendMessage("Help page is empty... (It's not supposed to be)").queue();
                System.out.println("The string for the help_message in info.java is empty.");
            }

        }
    }
}
