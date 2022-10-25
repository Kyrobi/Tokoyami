package me.kyrobi.Tokoyami.Auto.CountingModeration;

import me.kyrobi.Tokoyami.Main;
import me.kyrobi.Tokoyami.utils.Sqlite;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

public class CountingMod extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e){

        if(e.getChannel().getIdLong() == Main.countingChannelID){

            // Grabs the channel that we want to change settings in
            TextChannel channelInfo = e.getGuild().getTextChannelById("799121727250235392");

            System.out.println("A message was sent in the counting channel: " + e.getAuthor().getAsTag());

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

            int sent;
            Long previous;
            int expectedValue;

            //Try to convert the message to an int to do math
            try{
                sent = Integer.parseInt(e.getMessage().getContentRaw());
                previous = (Long) jsonObject.get("countingProgress");
            }

            //Assume that if it can't convert, the message isn't right because it's not an int in the first place and then delete
            catch(NumberFormatException error){
                System.out.println("Deleted " + e.getMessage().getContentRaw() + " from #counting" + " sent by " + e.getMember().getNickname() + " | " + e.getMember() + "\n");
                e.getMessage().delete().queueAfter(250, TimeUnit.MILLISECONDS);
                return;
            }

            expectedValue = (int) (previous + 1);
            if(expectedValue != sent){
                System.out.println("Deleted " + e.getMessage().getContentRaw() + " from #counting" + " sent by " + e.getMember().getNickname() + " | " + e.getMember());
                System.out.println("Previous: " + previous + ". Expecting: " + expectedValue + ". But user sent " + sent + "\n");
                e.getMessage().delete().queueAfter(250, TimeUnit.MILLISECONDS);
            }

            else{

                //Increment the counting data
                previous++;

                // Rip, rate limit only allows 2 changes per 10 minutes.
                //channelInfo.getManager().setTopic("Current Progess: " + previous + " Next number: " + expectedValue).queue();
                //System.out.println("Updating channel info");

                ///Writing the data to json

                // creating JSONObject to store the data
                JSONObject jo = new JSONObject();

                // putting data to JSONObject
                // Call put more if add multiple values
                jo.put("countingProgress", previous);

                //SQLite stuff
                Sqlite sqlite = new Sqlite();

                //If the user is not in the database, we add them to it
                if(sqlite.getCount(e.getAuthor().getIdLong()) == 0){
                    System.out.println("Creating new profile for user" + e.getAuthor().getAsTag());
                    sqlite.insert(e.getAuthor().getIdLong(), 1, e.getAuthor().getAsTag());
                }

                //If user already exists, increment their data
                else{
                    int currentAmount = sqlite.getAmount(e.getAuthor().getIdLong());
                    sqlite.update(e.getAuthor().getIdLong(), ++currentAmount, e.getAuthor().getAsTag());
                }

                //Write into the file
                try (FileWriter file = new FileWriter("data/counting.json"))
                {
                    file.write(jo.toString());
                    //System.out.println("Successfully updated json object to file...!!");
                }

                catch (IOException error){
                    System.out.println("Error writing to counting.json");
                }
            }
        }
    }

    // If the user updates their message in the counting channel, we kill them
    public void onMessageUpdate(MessageUpdateEvent e){
        if(e.getChannel().getIdLong() == Main.countingChannelID){

            // Grabs the channel that we want to change the permission in
            TextChannel channel = e.getGuild().getTextChannelById("459397281754513408");

            try{
                channel.createPermissionOverride(e.getMember())
                        .setDeny(Permission.MESSAGE_WRITE)
                        .queue();
            }
            catch(IllegalStateException ev){
                System.out.println("Can't update. User already has a PermissionOverride in this channel!");
            }

            System.out.println(e.getMember().getUser().getName() + " Updated message in counting");
        }
    }
}
