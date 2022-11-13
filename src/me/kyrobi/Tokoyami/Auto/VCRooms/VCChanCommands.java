package me.kyrobi.Tokoyami.Auto.VCRooms;

/*
- Lock / Unlock
- Hide / unhide
- Limit
- Rename
- Bitrate
 */


import me.kyrobi.Tokoyami.Main;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.EnumSet;

import static me.kyrobi.Tokoyami.Auto.VCRooms.ChannelCreator.tempTXTID;
import static me.kyrobi.Tokoyami.Auto.VCRooms.ChannelCreator.creatorID;
import static me.kyrobi.Tokoyami.Auto.VCRooms.ChannelCreator.tempVCID;
import static me.kyrobi.Tokoyami.Main.jda;

public class VCChanCommands extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e){

        // Don't do anything if command is issued by bot
        if(e.getAuthor().isBot()){
            return;
        }

        // Don't do anything if command isn't used in temp text channel
        if(!tempTXTID.contains(e.getChannel().getIdLong())){
            System.out.println("Not a temp channel");
            return;
        }

        // Don't do anything if the user isn't an owner of the text channel
        int creatorIndex = creatorID.indexOf(e.getMember().getIdLong());
        System.out.println("The creator index: " + creatorIndex);

        // If the current channel being typed in isn't equal to the text channel associated with the user
        if(e.getChannel().getIdLong() != tempTXTID.get(creatorIndex)){
            System.out.println("User doesn't own this text chanenl");
            return;
        }

        String[] args = e.getMessage().getContentRaw().split(" ");

        // Command for VC modifications
        if(args[0].equalsIgnoreCase(Main.prefix + "vc")){

            System.out.println("Bleh");

            Guild guild = e.getMember().getGuild();
            String atEveryoneID = guild.getId();

            VoiceChannel vc = jda.getVoiceChannelById(tempVCID.get(creatorIndex));


            if(args[1].equalsIgnoreCase("lock")){
                vc.upsertPermissionOverride(jda.getRoleById(atEveryoneID)).setDeny(Permission.VOICE_CONNECT).queue();
                e.getChannel().sendMessage("Channel locked!").queue();

            }

            else if(args[1].equalsIgnoreCase("unlock")){
                vc.upsertPermissionOverride(jda.getRoleById(atEveryoneID)).clear(Permission.VOICE_CONNECT).queue();
                e.getChannel().sendMessage("Channel unlocked!").queue();
            }

            else if(args[1].equalsIgnoreCase("hide")){
                vc.upsertPermissionOverride(jda.getRoleById(atEveryoneID)).setDeny(Permission.VIEW_CHANNEL).queue();
                e.getChannel().sendMessage("Channel hidden!").queue();
            }

            else if(args[1].equalsIgnoreCase("unhide")){
                vc.upsertPermissionOverride(jda.getRoleById(atEveryoneID)).clear(Permission.VIEW_CHANNEL).queue();
                e.getChannel().sendMessage("Channel unhidden!").queue();

            }

            else if(args[1].equalsIgnoreCase("limit")){
                int limit;
                try{
                    limit = Integer.parseInt(args[2]);
                }

                catch(NumberFormatException nfe){
                    e.getChannel().sendMessage("Please enter a valid integer. (i.e. $vc limit 10)").queue();
                    return;
                }

                vc.getManager().setUserLimit(limit).queue();

            }

        }

    }

}
