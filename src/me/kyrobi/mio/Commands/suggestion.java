package me.kyrobi.mio.Commands;

import me.kyrobi.mio.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.util.List;

public class suggestion extends ListenerAdapter {

    @Override //Overrides the super class
    public void onGuildMessageReceived(GuildMessageReceivedEvent e){
        String[] args = e.getMessage().getContentRaw().split(" "); // split up every argument since every argument has a space

        if(args[0].equalsIgnoreCase(Main.prefix + "suggest")){

            List<TextChannel> channelList = e.getGuild().getTextChannelsByName("suggestions", true);

            if(channelList.size() == 0){
                e.getChannel().sendMessage("There is not a suggestions channel in this server!");
                return;
            }

            TextChannel textChannel = channelList.get(0);

            StringBuilder suggestion = new StringBuilder();

            for(int i = 1; i < args.length; i++){
                suggestion.append(args[i] + " ");
            }

            if(suggestion.length() > 1024){
                e.getChannel().sendMessage("Your suggestion is too damn long (" + suggestion.length() + " characters)! Please limit it to 1024 characters.").queue();
                return;
            }

            Emote yesReaction = e.getJDA().getEmoteById("588622628751802382");
            Emote neutralReaction = e.getJDA().getEmoteById("976937355564957766");
            Emote noReaction = e.getJDA().getEmoteById("588622613417295872");


            EmbedBuilder eb = new EmbedBuilder();
            //eb.setTitle("Submitter", null);
            eb.setColor(Color.red);
            eb.setColor(new Color(0xF40C0C));
            eb.setColor(new Color(255, 0, 54));
            eb.addField("Submitter", e.getAuthor().getName(), false);
            eb.addField("Suggestion", String.valueOf(suggestion), false);
            //eb.addField("", "\n", false);
            eb.addBlankField(true);
            //eb.setAuthor("name", null, "https://github.com/zekroTJA/DiscordBot/blob/master/.websrc/zekroBot_Logo_-_round_small.png");
            eb.setFooter("Yes   |   Neutral    |   No");
            //eb.setImage("https://cdn.discordapp.com/attachments/845435268974116864/917359478964375572/MioFlap.gif");
            eb.setThumbnail("https://cdn.discordapp.com/attachments/821102901945958440/917368562543374366/ZKkEg1G_1.jpg");
            //e.getChannel().sendMessageEmbeds(eb.build()).queue();

            textChannel.sendMessageEmbeds(eb.build()).queue(message -> {
                message.addReaction(yesReaction).queue();
                message.addReaction(neutralReaction).queue();
                message.addReaction(noReaction).queue();
            });


            e.getChannel().sendMessage("Suggestion made!").queue();
        }
    }
}
