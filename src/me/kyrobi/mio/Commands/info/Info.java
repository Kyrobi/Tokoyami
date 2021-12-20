package me.kyrobi.mio.Commands.info;

import me.kyrobi.mio.Main;
import me.kyrobi.mio.objects.SystemInfo;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.io.*;
import java.util.Scanner;

import javax.sound.sampled.SourceDataLine;

public class Info extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e){
        String[] args = e.getMessage().getContentRaw().split(" ");

        if((args[0].equalsIgnoreCase(Main.prefix + "info")) || (args[0].equalsIgnoreCase(Main.prefix + "help"))){

            //long startTime = System.nanoTime();
            String help_message = constructInfoMessage();
            //long endTime = System.nanoTime();
            //long duration = (endTime - startTime); /// 1000000;
            //double elapsedTimeInSecond = (double) duration / 1_000_000_000;
            //System.out.printf("%.9f seconds\n", elapsedTimeInSecond);

            if(help_message == null){
                e.getChannel().sendMessage("Help page is empty... (It's not supposed to be)").queue();
                System.out.println("The string for the help_message in info.java is empty.");
            }

            else{
                e.getChannel().sendMessage(help_message).queue();
            }

        }
    }

    @Override
    public void onPrivateMessageReceived(PrivateMessageReceivedEvent ev){

        if(ev.getAuthor().isBot()){
            return;
        }

        String[] args = ev.getMessage().getContentRaw().split(" ");

        if((args[0].equalsIgnoreCase(Main.prefix + "info")) || (args[0].equalsIgnoreCase(Main.prefix + "help"))){
            String help_message = constructInfoMessage();

            ev.getChannel().sendMessage(help_message).queue();
        }
    }

    public String constructInfoMessage() {

        StringBuilder stringBuilder = new StringBuilder();  // For appending strings read from an external file
        String help_message = null;
        // Read the info message from a txt file
        try {
            FileReader reader = new FileReader("data/info.txt");

            // The reader returns an int value representing each character in the file. Returns -1 when end of file
            int data = reader.read();
            while (data != -1) {
                stringBuilder.append(String.valueOf((char) data));
                data = reader.read();
            }
            reader.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        help_message = stringBuilder.toString();

        try {
            return help_message;
        } catch (IllegalArgumentException message_empty) {
            return null;
        }
    }

    // This does not respect blank lines :(

//        try {
//            // FileInputStream opens the given file and produces an InputStream from it
//            FileInputStream buffer = new FileInputStream("data/info.txt");
//
//            Scanner scanner = new Scanner(buffer);
//
//            // The iterative method
//            while(scanner.hasNextLine()) {
//                stringBuilder.append(scanner.nextLine()).append('\n');
//            }
//
//            // System.out.println(stringBuilder.toString());
//            scanner.close();
//            return stringBuilder.toString();
//        }
//
//        catch (IOException e) {
//            System.out.println("Something went wrong reading info.txt");
//        }
//        return "error";
//    }
}
