package me.kyrobi.Tokoyami.Auto.VCRooms;

/*
- Lock / Unlock
- Hide / unhide
- Limit
- Rename
- Bitrate
 */


import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;


import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static me.kyrobi.Tokoyami.Auto.VCRooms.ChannelCreator.tempTXTID;
import static me.kyrobi.Tokoyami.Auto.VCRooms.ChannelCreator.creatorID;
import static me.kyrobi.Tokoyami.Auto.VCRooms.ChannelCreator.tempVCID;
import static me.kyrobi.Tokoyami.Main.jda;

public class VCChanCommands extends ListenerAdapter {

    String atEveryoneID;
    int creatorIndex;
    long vcChannelOwnerID;
    File dbfile = new File("");
    String url = "jdbc:sqlite:" + dbfile.getAbsolutePath() + "/data.db";

    @Override
    public void onButtonClick(ButtonClickEvent e){

        Member author = e.getMember();
        String command = e.getComponentId();

        // Don't do anything if command is issued by bot
        if(author.getUser().isBot()){
            return;
        }

        if(!creatorID.contains(e.getMember().getIdLong())){
            e.reply(e.getMember().getAsMention() + " You cannot use this option because you are not the owner of this voice channel!").queue(interactionHook ->{
                interactionHook.deleteOriginal().queueAfter(5, TimeUnit.SECONDS);
                    });
            return;
        }

        // If the current channel being typed in isn't equal to the text channel associated with the user
        if(e.getChannel().getIdLong() != tempTXTID.get(creatorIndex)){
            System.out.println("User doesn't own this text channel");
            return;
        }

        // Don't do anything if command isn't used in temp text channel
        if(!tempTXTID.contains(e.getChannel().getIdLong())){
            return;
        }

        // Don't do anything if the user isn't an owner of the text channel
        creatorIndex = creatorID.indexOf(e.getMember().getIdLong());
        vcChannelOwnerID = creatorID.get(creatorIndex);
        System.out.println("The creator index: " + creatorIndex);


        Guild guild = e.getMember().getGuild();
        atEveryoneID = guild.getId();


        switch (command){
            case "lockvc":
                lockVC(e);
                break;
            case "unlockvc":
                unlockVC(e);
                break;
            case "hidevc":
                hideVC(e);
                break;
            case "unhidevc":
                unhideVC(e);
                break;
            case "savemembers":
                savePreset(e);
                break;
            case "loadmembers":
                loadPreset(e.getGuild(), e);
                break;
        }

    }

    private void lockVC(ButtonClickEvent e){
        VoiceChannel vc = jda.getVoiceChannelById(tempVCID.get(creatorIndex));
        vc.upsertPermissionOverride(jda.getRoleById(atEveryoneID)).setDeny(Permission.VOICE_CONNECT).queue();
        e.reply("Channel locked!").queue();
    }

    private void unlockVC(ButtonClickEvent e){
        VoiceChannel vc = jda.getVoiceChannelById(tempVCID.get(creatorIndex));
        vc.upsertPermissionOverride(jda.getRoleById(atEveryoneID)).clear(Permission.VOICE_CONNECT).queue();
        e.reply("Channel unlocked!").queue();
    }

    private void hideVC(ButtonClickEvent e){
        VoiceChannel vc = jda.getVoiceChannelById(tempVCID.get(creatorIndex));
        vc.upsertPermissionOverride(jda.getRoleById(atEveryoneID)).setDeny(Permission.VIEW_CHANNEL).queue();
        e.reply("Channel hidden!").queue();
    }

    private void unhideVC(ButtonClickEvent e){
        VoiceChannel vc = jda.getVoiceChannelById(tempVCID.get(creatorIndex));
        vc.upsertPermissionOverride(jda.getRoleById(atEveryoneID)).clear(Permission.VIEW_CHANNEL).queue();
        e.reply("Channel unhidden!").queue();
    }


    //Saving the channel's members
    private void savePreset(ButtonClickEvent e){

        System.out.println(e.getMember().getEffectiveName() + " saved channel members");

        ArrayList<Long> userID = new ArrayList<>();

        VoiceChannel vc = jda.getVoiceChannelById(tempVCID.get(creatorIndex));
        List<PermissionOverride> list = vc.getMemberPermissionOverrides();


        for(PermissionOverride i :list){
            //System.out.print(i.getIdLong()+ "\n");
            userID.add(i.getIdLong());
        }

        StringBuilder userIDString = new StringBuilder();
        for(Long i:userID){
            userIDString.append(i);
            userIDString.append(",");
        }


        // If the user already has vc members saved, we just update it
        if(SQLifExists(vcChannelOwnerID)){
            SQLupdateMembers(vcChannelOwnerID, userIDString.toString());
        }

        //else, we insert the values into the database
        else{
            SQLsaveMembers(vcChannelOwnerID, userIDString.toString());
        }

        e.reply("Member list saved!").queueAfter(1, TimeUnit.SECONDS);
    }

    private void loadPreset(Guild guild, ButtonClickEvent e){
        System.out.println(e.getMember().getEffectiveName() + " loaded channel members");

        String[] members = SQLgetMembers(vcChannelOwnerID).split(",");

        VoiceChannel vc = jda.getVoiceChannelById(tempVCID.get(creatorIndex));
        Member memberToAdd;

        for(String member:members) {
            User user = jda.getUserById(member);
            memberToAdd = guild.getMember(user);
            try {
                vc.createPermissionOverride(memberToAdd).setAllow(EnumSet.of(Permission.VIEW_CHANNEL, Permission.VOICE_CONNECT)).queueAfter(500, TimeUnit.MILLISECONDS);
            } catch (IllegalStateException ex) {
                //Nothing
            }
        }

        e.reply("Member list loaded!").queueAfter(1, TimeUnit.SECONDS);

    }


    private void SQLsaveMembers(long chanCreatorID, String members){
        String sqlcommand = "INSERT INTO vcmembers(userID, members) VALUES(?,?)";

        try{
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection(url);
            PreparedStatement stmt = conn.prepareStatement(sqlcommand);
            stmt.setLong(1, chanCreatorID); // The first column will contain the ID
            stmt.setString(2, members); // The second column will contain the amount
            stmt.executeUpdate();
            conn.close();
        }
        catch(SQLException | ClassNotFoundException error){
            System.out.println(error.getMessage());
        }
    }

    private void SQLupdateMembers(long chanCreatorID, String members){

        try{
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection(url);
            //PreparedStatement stmt = conn.prepareStatement(updateCommand);
            PreparedStatement update = conn.prepareStatement("UPDATE vcmembers SET members = ? WHERE userID = ?");

            update.setString(1, members);
            update.setLong(2, chanCreatorID);

            update.executeUpdate();
            conn.close();

        }
        catch(SQLException | ClassNotFoundException e){
            System.out.println(e.getMessage());
        }
    }

    private String SQLgetMembers(long ownerID){
        StringBuilder members = new StringBuilder();

        try{
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection(url);
            //PreparedStatement stmt = conn.prepareStatement(updateCommand);
            PreparedStatement getMembers = conn.prepareStatement("SELECT members FROM vcmembers WHERE userID = ?");

            getMembers.setLong(1, ownerID);

            ResultSet rs = getMembers.executeQuery();

            members = new StringBuilder(rs.getString("members"));

            conn.close();

        }
        catch(SQLException | ClassNotFoundException e){
            System.out.println(e.getMessage());
        }

        return members.toString();

    }


    private Boolean SQLifExists(long ownerID){
        String selectfrom = "SELECT * FROM vcmembers";
        int count = 0;

        try{
            //System.out.println("Connecting...");
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection(url); // Make connection
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(selectfrom); // Execute the command


            //We loop through the database. If the userID matches, we break out of the loop
            while(rs.next()){
                //System.out.println("ID: " + rs.getString("userId") + " Amount: " + rs.getInt("amount"));
                if(Objects.equals(rs.getLong("userID"), ownerID)){
                    ++count;
                    rs.close();
                    conn.close();
                    break; // Breaks out of the loop once the value has been found. No need to loop through the rest of the database
                }
            }

            //System.out.println("Count: " + count);
        }
        catch(SQLException | ClassNotFoundException e){
            e.printStackTrace();
            System.out.println("Error code: " + e.getMessage());
        }

        if(count != 0){
            return true;
        }
        else{
            return false;
        }
    }

}
