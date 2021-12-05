package me.kyrobi.mio;

import me.kyrobi.mio.Auto.CountingMod;
import me.kyrobi.mio.Commands.Debug;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.lang.System.exit;

public class Main {

    public static JDA jda;
    public static String prefix = ">";

    public static void main(String[] args) throws LoginException, IOException {

        Path tokenFile;
        String token = null;

        //Read in token from a file
        try{
            tokenFile = Path.of("token.txt");
            token = Files.readString(tokenFile);
            jda = JDABuilder.createDefault(token).enableIntents(GatewayIntent.GUILD_MESSAGES).build();
        }
        catch (IOException | IllegalArgumentException e){
            System.out.println("Cannot open token file!");
            exit(1);
        }

        jda.getPresence().setActivity(Activity.watching("Lewds"));
        jda.getPresence().setStatus(OnlineStatus.ONLINE);

        // Registers from class
        jda.addEventListener(new Debug());
        jda.addEventListener(new CountingMod());
    }
}
