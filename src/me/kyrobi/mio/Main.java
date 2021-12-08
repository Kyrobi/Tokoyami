package me.kyrobi.mio;

import me.kyrobi.mio.Auto.*;
import me.kyrobi.mio.Commands.*;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.lang.System.exit;

public class Main {

    public static JDA jda;
    public static String prefix = "m!";

    public static long countingChannelID;
    public static long announcementChannelID;


    public static void main(String[] args) throws LoginException, IOException {

        String watchingStatus;

        Path tokenFile;
        String token = null;

        //Read in token from a file
        try{
            tokenFile = Path.of("token.txt");
            token = Files.readString(tokenFile);
            jda = JDABuilder.createDefault(token).enableIntents(GatewayIntent.GUILD_MESSAGES).build();
        }
        catch (IOException | IllegalArgumentException e){
            System.out.println("Cannot open token file! Making a new one. Please configure it");

            PrintWriter writer = new PrintWriter("token.txt", "UTF-8");
            writer.print("1234567890123456");
            writer.close();
            exit(1);
        }

        //Reading config file
        String config = null;
        Path configFile;
        try{
            configFile = Path.of("data/config.json");
            config = Files.readString(configFile);
        }

        //If a config file does not exist, we create a new one with some default values
        catch (IOException | IllegalArgumentException e){
            System.out.println("Config file does not exist! Creating a new one. Please configure it");
            String configFormat = "{\n\"countingChannelID\":1234567890," +
                    "\n\"announcementChannelID\":1234567890," +
                    "\n\"setWatchingMessage\":\"Movies\"" +
                    "\n}";

            PrintWriter writer = new PrintWriter("data/config.json", "UTF-8");
            writer.print(configFormat);
            writer.close();

            exit(1);
        }

        Object obj= JSONValue.parse(config);
        JSONObject jsonObject = (JSONObject) obj;  //creating an object of JSONObject class and casting the object into JSONObject type

        //Set the variables by reading from the json file
        countingChannelID = (Long) jsonObject.get("countingChannelID");
        announcementChannelID = (Long) jsonObject.get("announcementChannelID");
        watchingStatus = (String) jsonObject.get("setWatchingMessage");

        System.out.println("countingChannel: "+countingChannelID);
        System.out.println("announcementChannel: "+announcementChannelID);
        System.out.println("setWatchingMessage: "+watchingStatus);

        jda.getPresence().setActivity(Activity.watching(watchingStatus));
        jda.getPresence().setStatus(OnlineStatus.ONLINE);

        // Registers from class
        jda.addEventListener(new Debug());
        jda.addEventListener(new CountingMod());
        jda.addEventListener(new AnnouncementReaction());
        jda.addEventListener(new Info());
    }
}
