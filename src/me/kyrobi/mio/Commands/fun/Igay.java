package me.kyrobi.mio.Commands.fun;

import me.kyrobi.mio.Main;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Random;

import static me.kyrobi.mio.Main.gayLordRoleID;

public class Igay extends ListenerAdapter {

    Random r = new Random();

    @Override //Overrides the super class
    public void onGuildMessageReceived(GuildMessageReceivedEvent e){
        String[] args = e.getMessage().getContentRaw().split(" "); // split up every argument since every argument has a space

        if(args[0].equalsIgnoreCase(Main.prefix + "igay")){

            if(!(e.getMessage().getChannel().getName().equals("spam"))){
                e.getChannel().sendMessage("This command can only be used in the #spam channel!").queue();
                return;
            }


            //int gayPercentage = Math.floor((Math.random() * 10001));
            //Create random number 0 - 99
            double gayPercentage = r.nextInt(10000) + 1;
//            int gayPercentageLoop = 0;
//
//
//            while(gayPercentage != 10000){
//                gayPercentageLoop++;
//                gayPercentage = Math.floor(r.nextInt(10000) + 1);
//            }
//            System.out.println("It took " + gayPercentageLoop + " tries to get the gaylord rank");


            String gayRatingMessage = null;
            if (gayPercentage >= 0 && gayPercentage <= 2000){
                gayPercentage = gayPercentage / 100;
                gayRatingMessage = "**Not Very Gay**";
            }
            //True if value range from 21 - 40
            else if (gayPercentage >= 2001 && gayPercentage <= 4000){
                gayPercentage = gayPercentage / 100;
                gayRatingMessage = "**Little Bit Gay**";
            }
            //True if value range from 41 - 60
            else if (gayPercentage >= 4001 && gayPercentage <= 6000){
                gayPercentage = gayPercentage / 100;
                gayRatingMessage = "**Decently Gay**";
            }
            //True if value range from 61 - 80
            else if (gayPercentage >= 6001 && gayPercentage <= 8000){
                gayPercentage = gayPercentage / 100;
                gayRatingMessage = "**Pretty Gay**";
            }
            //True if value range from 81 - 90
            else if (gayPercentage >= 8001 && gayPercentage <= 9000){
                gayPercentage = gayPercentage / 100;
                gayRatingMessage = "**Very Gay**";
            }

            else if (gayPercentage >= 9001 && gayPercentage <= 9999){
                gayPercentage = gayPercentage / 100;
                gayRatingMessage = "**HELLA GAY**";
            }

            else if (gayPercentage == 10000){

                gayRatingMessage = "**Gay Lord!** (You also got the GayLord rank on Discord)";
                e.getGuild().addRoleToMember(e.getMember(), e.getGuild().getRoleById(gayLordRoleID)).queue();
                gayPercentage = 100;
            }
            e.getChannel().sendMessage(e.getMember().getAsMention() + " is " + (String.format("%.0f", Math.floor(gayPercentage))) + "% gay. This means they are " + gayRatingMessage).queue();
            return;
        }
    }
}
