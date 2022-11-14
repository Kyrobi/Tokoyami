package me.kyrobi.Tokoyami.Auto.VCRooms;


import me.kyrobi.Tokoyami.Main;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Button;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.concurrent.TimeUnit;



/*
TODO
1. Add permissions for channels
2. Add commands to change channel attributes
3. Create a text channel to type commands in
 */

public class ChannelCreator extends ListenerAdapter {

    //For some reason, hashmaps doesn't work the way I want it comes to lambda functions ... so I'm using the ordered property of ArrayList to accomplish what a hashmap does
    public static ArrayList<Long> tempVCID = new ArrayList<>(); // Keeps track of temp channels. Voice channel ID, Txt channel id
    public static ArrayList<Long> tempTXTID = new ArrayList<>();
    public static ArrayList<Long> creatorID = new ArrayList<>();

    // TODO (Possibly?) Save the temp channels to an external source so that we don't lose track of which channels are temp ones upon bot restart...

    /*
    Triggers when a user joins the voice channel
     */
    @Override
    public void onGuildVoiceJoin(GuildVoiceJoinEvent e){
        //System.out.println(e.getVoiceState().getChannel().getMembers().size());
        if(e.getChannelJoined().getIdLong() == Main.newVCChannel){

            //Reference: https://stackoverflow.com/questions/60088401/how-to-create-a-private-channel-in-a-discord-server-not-a-user-bot-dm-using-jd

            Guild guild = e.getMember().getGuild();

            //Category to create new voice channels under
            Category category = e.getGuild().getCategoryById(Main.voiceChannelCategory);

            if(category == null){
                System.out.println("Category does not exist for creating voice channels!");
            }

            // This creates the actual voice channel
            guild.createVoiceChannel(e.getMember().getEffectiveName() + "'s vc")
                    .addPermissionOverride(e.getGuild().getSelfMember(), EnumSet.of(Permission.MANAGE_CHANNEL, Permission.MANAGE_PERMISSIONS, Permission.VIEW_CHANNEL), null)
                    .setParent(category)

            // This function returns back a voice channel object which we can use
            // to move the user to that newly created channel
            .queue(voiceChannel -> {
                guild.moveVoiceMember(e.getMember(), voiceChannel).queueAfter(500, TimeUnit.MILLISECONDS);
                voiceChannel.createPermissionOverride(e.getMember()).setAllow(EnumSet.of(Permission.MANAGE_CHANNEL, Permission.MANAGE_PERMISSIONS, Permission.VIEW_CHANNEL)).queue();
                tempVCID.add(voiceChannel.getIdLong());
                creatorID.add(e.getMember().getIdLong());
            });

            /*
            This creates a temp text channel for type user commands
            No one will be able to view it unless they enter the vc
             */
            String atEveryoneID = guild.getId();

            guild.createTextChannel(e.getMember().getEffectiveName() + "'s vc")
                    .addRolePermissionOverride(guild.getRoleById(atEveryoneID).getIdLong(), null, EnumSet.of(Permission.VIEW_CHANNEL))
                    .addPermissionOverride(e.getGuild().getSelfMember(), EnumSet.of(Permission.MANAGE_CHANNEL, Permission.MANAGE_PERMISSIONS), null)
                    .setParent(category)
                    .queue(textChannel -> {
                        textChannel.createPermissionOverride(e.getMember()).setAllow(EnumSet.of(Permission.VIEW_CHANNEL)).queue();
                        tempTXTID.add(textChannel.getIdLong());

                        Button lockButton = Button.primary("lockvc", "Lock");
                        Button unlockButton = Button.primary("unlockvc", "Unlock");
                        Button hideButton = Button.primary("hidevc", "Hide");
                        Button unhideButton = Button.primary("unhidevc", "Unhide");
                        Button saveMembersButton = Button.secondary("savemembers", "Save VC Members");
                        Button loadMembersButton = Button.secondary("loadmembers", "Load VC Members");

//                        textChannel.sendMessage("""
//                                Voice Chat Options:
//                                ----------
//                                `$vc lock` - Locks the vc so others can't join
//                                `$vc unlock` - Unlocks the vc
//                                `$vc hide` - Hides the vc from others
//                                `$vc unhide` - Unhides the vc from others
//                                `$vc limit (amount)` - Sets the user limit for the voice channel\040
//                                """).queue();

                        Message message = new MessageBuilder()
                                .append("Voice Chat Options")
                                .setActionRows(ActionRow.of(lockButton, unlockButton, hideButton, unhideButton)
                                        , ActionRow.of(saveMembersButton, loadMembersButton))
                                .build();

                        Message presetMessage = new MessageBuilder()
                                .append("""
                                        **Save Members**\s
                                        This will save the members you have added to the vc channel so that they can be quickly added later.
                                        """)
                                .build();

                        textChannel.sendMessage(message).queue();
                        textChannel.sendMessage(presetMessage).queueAfter(1, TimeUnit.SECONDS);
                    });


        }

        //Gives permission to view temp text channel for user once they join a temp vc
        else{
            // Check if this is a temp channel
            if(tempVCID.contains(e.getChannelJoined().getIdLong())){
                int index = tempVCID.indexOf(e.getChannelJoined().getIdLong());
                long tempTxtID = tempTXTID.get(index);

                Guild guild = e.getGuild();

                //If person doesn't already have the view channel role, add it
                if(guild.getTextChannelById(tempTxtID).getPermissionOverride(e.getMember()) == null){
                    guild.getTextChannelById(tempTxtID).createPermissionOverride(e.getMember()).setAllow(Permission.VIEW_CHANNEL).queue();
                }

            }
        }
    }



    /*
    Triggers when a user moves into the channel from another voice channel
     */
    @Override
    public void onGuildVoiceMove(GuildVoiceMoveEvent e){

        deleteChannel(e.getChannelLeft());

        //If a user is leaving a temp vc
        if(tempVCID.contains(e.getChannelLeft().getIdLong())){
            int index = tempVCID.indexOf(e.getChannelLeft().getIdLong());
            long tempTxtID = tempTXTID.get(index);

            Guild guild = e.getGuild();

            // Remove user's permission
            // If the creator of this vc leaves
            if(creatorID.get(index) == e.getMember().getIdLong()){
                // Nothing
            }
            else{
                //guild.getTextChannelById(tempTxtID).createPermissionOverride(e.getMember()).clear().queue();
                guild.getTextChannelById(tempTxtID).getPermissionOverride(e.getMember()).delete().queue();
            }
        }

        if(tempVCID.contains(e.getChannelJoined().getIdLong())){
            int index = tempVCID.indexOf(e.getChannelJoined().getIdLong());
            long tempTxtID = tempTXTID.get(index);

            Guild guild = e.getGuild();

            //If person doesn't already have the view channel role, add it
            if(guild.getTextChannelById(tempTxtID).getPermissionOverride(e.getMember()) == null){
                guild.getTextChannelById(tempTxtID).createPermissionOverride(e.getMember()).setAllow(Permission.VIEW_CHANNEL).queue();
            }

        }
    }


    /*
    Triggers when a user leaves the voice channel
     */
    @Override
    public void onGuildVoiceLeave(GuildVoiceLeaveEvent e){
        deleteChannel(e.getChannelLeft());

        //If a user is leaving a temp vc
        if(tempVCID.contains(e.getChannelLeft().getIdLong())){
            int index = tempVCID.indexOf(e.getChannelLeft().getIdLong());
            long tempTxtID = tempTXTID.get(index);

            Guild guild = e.getGuild();

            // Remove user's permission
            // If the creator of this vc leaves
            if(creatorID.get(index) == e.getMember().getIdLong()){
                // Nothing
            }
            else{
                // Removes the user from the channel's permission
                guild.getTextChannelById(tempTxtID).getPermissionOverride(e.getMember()).delete().queue();
            }
        }
    }


    /*
    Checks for deleting a channel
     */
    private void deleteChannel(VoiceChannel e) {

        int botsInVC = 0;
        long vcID;

        vcID = e.getIdLong();


        // Check for bots and subtract from the actual member count in the channel
        for(Member member:e.getMembers()){
            if(member.getUser().isBot()){
                ++botsInVC;
            }
        }

        int VCmemberCount = e.getMembers().size() - botsInVC;

        if(VCmemberCount == 0 && tempVCID.contains(vcID)){
            Guild guild = e.getGuild();
            VoiceChannel vc = guild.getVoiceChannelById(vcID);
            TextChannel txt = guild.getTextChannelById(tempTXTID.get(tempVCID.indexOf(vcID)));

            // We loop through the channel to kick all the bots out or else deleting the channel is gonna have errors
            for(Member member: e.getMembers()){
                if(member.getUser().isBot()){
                    guild.kickVoiceMember(member).queue();
                }
            }
            // Actually delete the channel
            int index = tempVCID.indexOf(vcID);

            //Since arrayList is ordered, we can assume that vc channel and txt channel are in the same index
            tempVCID.remove(index);
            tempTXTID.remove(index);
            creatorID.remove(index);

            vc.delete().queueAfter(500, TimeUnit.MILLISECONDS);
            txt.delete().queueAfter(500, TimeUnit.MILLISECONDS);
        }
    }
}
