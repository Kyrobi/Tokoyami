package me.kyrobi.mio.Commands.info.counting;

import me.kyrobi.mio.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.RestAction;

import java.awt.*;
import java.io.File;
import java.sql.*;
import java.util.Objects;

import static me.kyrobi.mio.Main.jda;

public class CountingLeaderboard extends ListenerAdapter {

    @Override //Overrides the super class
    public void onGuildMessageReceived(GuildMessageReceivedEvent e){
        String[] args = e.getMessage().getContentRaw().split(" "); // split up every argument since every argument has a space

        if((args[0].equalsIgnoreCase(Main.prefix + "counting") && args[1].equalsIgnoreCase( "leaderboard"))){

            File dbfile = new File(".");
            String url = "jdbc:sqlite:" + dbfile.getAbsolutePath() + "\\counting.db";
            String selectDescOrder = "SELECT * FROM `stats` ORDER BY `amount` DESC";

            StringBuilder stringBuilder1 = new StringBuilder();
            StringBuilder stringBuilder2 = new StringBuilder();
            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle("Counting Leaderboard", null);

            try{
                Connection conn = DriverManager.getConnection(url); // Make connection
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(selectDescOrder); // Execute the command


                //We loop through the database. If the userID matches, we break out of the loop
                while(rs.next()){
                    long userId = rs.getLong("userId");

                    //When we loop, we append to a massive string that will be used later
                    stringBuilder1.append("\n" + (toUser(rs.getLong("userId"))).getAsMention());
                    stringBuilder2.append("\n" + rs.getInt("amount"));
                }
            }
            catch(SQLException ev){
                ev.printStackTrace();
                System.out.println("Error code: " + ev.getMessage());
            }

            //We take the final string and post it into the field
            eb.addField("Name", stringBuilder1.toString(), true);
            eb.addField("Amount", stringBuilder2.toString(), true);

            e.getChannel().sendMessageEmbeds(eb.build()).queue();
        }
    }

    //Some rest action bs. Needed this code for it to work
    public User toUser(long id){
        return jda.retrieveUserById(id).complete();
    }
}
