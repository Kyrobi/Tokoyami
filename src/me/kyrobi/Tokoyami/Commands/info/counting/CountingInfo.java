package me.kyrobi.Tokoyami.Commands.info.counting;

import me.kyrobi.Tokoyami.Main;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class CountingInfo extends ListenerAdapter {

    @Override //Overrides the super class
    public void onGuildMessageReceived(GuildMessageReceivedEvent e){
        String[] args = e.getMessage().getContentRaw().split(" "); // split up every argument since every argument has a space

        if((args[0].equalsIgnoreCase(Main.prefix + "cprogress"))){

            //Read from Json
            String data = null;
            Path configFile;

            try{
                configFile = Path.of("data/counting.json");
                data = Files.readString(configFile);
            }
            catch (IOException | IllegalArgumentException error){
                System.out.println("Unable to open counting.json");
                error.printStackTrace();
            }

            Object obj= JSONValue.parse(data); // Reads the value of .json into string
            JSONObject jsonObject = (JSONObject) obj;

            long previous = (long) jsonObject.get("countingProgress");
            long expected = previous;
            expected++;

            e.getChannel().sendMessage("Current: " + previous + " Next: " + expected).queue();
        }
    }
}
