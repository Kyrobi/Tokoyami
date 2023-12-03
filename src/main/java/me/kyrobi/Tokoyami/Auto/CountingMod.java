package me.kyrobi.Tokoyami.Auto;

import me.kyrobi.Tokoyami.Main;
import me.kyrobi.Tokoyami.utils.Sqlite;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static me.kyrobi.Tokoyami.Main.bannedCountingUsers;
import static me.kyrobi.Tokoyami.Main.jda;

public class CountingMod extends ListenerAdapter {

    public static HashMap<Long, UserMessageObject> messageCache = new HashMap<>();

    @Override
    public void onMessageReceived(MessageReceivedEvent e){

        if(e.getChannel().getIdLong() == Main.countingChannelID){

            // Stores the message in cache
            UserMessageObject userMessage = new UserMessageObject(e.getMessageIdLong(), e.getAuthor().getName(), e.getMessage().getContentRaw());
            messageCache.put(e.getMessage().getIdLong(), userMessage);

            // Grabs the channel that we want to change settings in

            TextChannel channelInfo = e.getGuild().getTextChannelById("799121727250235392");

            logMessage(
                    "A message was sent in the counting channel: " + e.getAuthor().getAsTag() + "\n" +
                            "Sent: `" + e.getMessage().getContentRaw() + "`"
            );

            if(bannedCountingUsers.contains(String.valueOf(e.getAuthor().getIdLong()))){
                e.getMessage().delete().queueAfter(100, TimeUnit.MILLISECONDS);
                return;
            }

            //Read from Json
            String data = null;
            Path configFile;

            try{
                configFile = Path.of("data/counting.json");
                data = Files.readString(configFile);
            }
            catch (IOException | IllegalArgumentException error){
                logMessage("Unable to open counting.json");
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

                logMessage("Deleted `" + e.getMessage().getContentRaw() + "` from #counting" + " sent by " + e.getMember().getEffectiveName() + "\n"
                        + e.getMember().getEffectiveName() + "\n");
                e.getMessage().delete().queueAfter(250, TimeUnit.MILLISECONDS);
                return;
            }

            expectedValue = (int) (previous + 1);
            if(expectedValue != sent){

                logMessage(
                        "Deleted " + e.getMessage().getContentRaw() + " from #counting" + " sent by " + e.getMember().getEffectiveName() + "\n" +
                        "Previous: " + previous + ". Expecting: " + expectedValue + ". But user sent " + sent + "\n"
                );

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
                   logMessage("Creating new profile for user" + e.getAuthor().getAsTag());
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
                    logMessage("Error writing to counting.json");
                }

                cleanMessageCache();
            }
        }
    }

    @Override
    public void onMessageDelete(MessageDeleteEvent e){
        if(e.getChannel().getIdLong() == Main.countingChannelID){

            String channelID = "1128712195090427954";
            TextChannel logChannel = jda.getTextChannelById(channelID);

            if(messageCache.containsKey(e.getMessageIdLong())){
                logChannel.sendMessage("**Message was deleted**\n"
                        + "Message author: " + messageCache.get(e.getMessageIdLong()).getUsername() + "\n"
                        + "Message content: " + messageCache.get(e.getMessageIdLong()).getUserMessage()
                ).queue();
            }

            else {
                logChannel.sendMessage("**Message was deleted**\n"
                        + "Message author: Not in cache" + "\n"
                        + "Message content: Not in cache"
                ).queue();
            }
        }
    }

     // If the user updates their message in the counting channel, we kill them
     @Override
    public void onMessageUpdate(MessageUpdateEvent e){
        if(e.getChannel().getIdLong() == Main.countingChannelID){

            String channelID = "1128695744946241627";
            TextChannel logChannel = jda.getTextChannelById(channelID);

            String messageId = e.getMessageId();

            // Fetch the old message
            MessageHistory history = new MessageHistory(e.getChannel());
            history.retrievePast(1).queue(messages -> {
                Message oldMessage = messages.get(0);
                String oldMessageContent = oldMessage.getContentRaw();
                String newMessageContent = e.getMessage().getContentRaw();

                logChannel.sendMessage(
                        "**User " + e.getMember().getUser().getName() + " updated the message in counting.**\n"
                                + "Old message: `" + oldMessageContent + "`\n"
                                + "New message: `" + newMessageContent + "`\n"
                ).queue();
            });

//            // Grabs the channel that we want to change the permission in
//            TextChannel channel = e.getGuild().getTextChannelById("459397281754513408");
//
//            try{
//                channel.upsertPermissionOverride(e.getMember())
//                        .setDeny(Permission.MESSAGE_WRITE)
//                        .queue();
//            }
//            catch(IllegalStateException ev){
//                System.out.println("Can't update. User already has a PermissionOverride in this channel!");
//            }
//
//            System.out.println(e.getMember().getUser().getName() + " Updated message in counting");
        }
    }

    private void logMessage(String message){
        String channelID = "1128695744946241627";
        TextChannel logChannel = jda.getTextChannelById(channelID);

        logChannel.sendMessage(message).queue();
        System.out.println(message);
    }

    private void cleanMessageCache(){
        for (Map.Entry<Long, UserMessageObject> messageTable : messageCache.entrySet()) {

            long key = messageTable.getKey();
            long dTime = System.currentTimeMillis() - messageCache.get(key).getTime();
            // 7 days in milliseconds
            if(dTime >= 604_800_000){
                // We remove messages in the cache that are older than 7 days
                messageCache.remove(key);
            }
        }
    }
}
