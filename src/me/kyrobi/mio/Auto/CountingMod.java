package me.kyrobi.mio.Auto;

import me.kyrobi.mio.Main;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class CountingMod extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e){
        if(e.getChannel().getIdLong() == Main.countingChannelID){
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
                System.out.println("Deleted " + e.getMessage().getContentRaw() + " from #counting" + " sent by " + e.getMember().getNickname() + " | " + e.getMember() + "\n");
                e.getMessage().delete().queue(); //Assume that if it can't convert, the message isn't right because it's not an int in the first place
                return;
            }

            expectedValue = previous + 1;
            if(expectedValue != sent){
                System.out.println("Deleted " + e.getMessage().getContentRaw() + " from #counting" + " sent by " + e.getMember().getNickname() + " | " + e.getMember());
                System.out.println("Previous: " + previous + ". Expecting: " + expectedValue + ". But user sent " + sent + "\n");
                e.getMessage().delete().queue();
            }

            else{
                //Read from Json
                Long progress;
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
                progress = (Long) jsonObject.get("countingProgress");

                //Increment the counting data
                progress++;

                ///Writing the data to json

                // creating JSONObject to store the data
                JSONObject jo = new JSONObject();

                // putting data to JSONObject
                // Call put more if add multiple values
                jo.put("countingProgress", progress);
                //jo.put("test", 5);

                //Write into the file
                try (FileWriter file = new FileWriter("data/counting.json"))
                {
                    file.write(jo.toString());
                    //System.out.println("Successfully updated json object to file...!!");
                }

                catch (IOException error){
                    System.out.println("Error writing to counting.json");
                }

//                // writing JSON to file:"counting.json" in cwd
//                PrintWriter pw = null;
//                try {
//                    pw = new PrintWriter("data/counting.json");
//                } catch (FileNotFoundException ex) {
//                    ex.printStackTrace();
//                }
//
//                //Writing the data to the file
//                pw.write(jo.toJSONString());
//
//                pw.flush();
//                pw.close();

            }
        }
    }
}
