package me.kyrobi.Tokoyami.Commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.emoji.CustomEmoji;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.modals.ModalInteraction;

import java.awt.*;
import java.util.Objects;

import static me.kyrobi.Tokoyami.Main.jda;

public class ModalListener extends ListenerAdapter {



    @Override
    public void onModalInteraction(ModalInteractionEvent e){

        if(e.getModalId().equals("suggestion-modal")){

            String suggestion_summary = e.getValue("suggestion-summary").getAsString();
            String suggestion_benefits = e.getValue("suggestion-description").getAsString();
            String suggestion_against = e.getValue("suggestion-description-against").getAsString();

            CustomEmoji yesReaction = e.getJDA().getEmojiById("588622628751802382");
            CustomEmoji neutralReaction = e.getJDA().getEmojiById("976937355564957766");
            CustomEmoji noReaction = e.getJDA().getEmojiById("588622613417295872");

            EmbedBuilder eb = new EmbedBuilder();
            //eb.setTitle("Submitter", null);
            eb.setColor(Color.red);
            eb.setColor(new Color(0xF40C0C));
            eb.setColor(new Color(255, 0, 54));
            eb.addField("Submitter", Objects.requireNonNull(e.getMember()).getNickname() + " | " + e.getMember().getEffectiveName(), false);

            eb.addField("Suggestion Summary", String.valueOf(suggestion_summary), false);
            eb.addField("Explanation on why this suggestion should be implemented:", String.valueOf(suggestion_benefits), false);
            eb.addField("Explanation on why this suggestion should NOT be implemented:", String.valueOf(suggestion_against), false);

            //eb.addField("", "\n", false);
            eb.addBlankField(true);
            //eb.setAuthor("name", null, "https://github.com/zekroTJA/DiscordBot/blob/master/.websrc/zekroBot_Logo_-_round_small.png");
            eb.setFooter("Yes   |   Neutral    |   No");
            //eb.setImage("https://cdn.discordapp.com/attachments/845435268974116864/917359478964375572/MioFlap.gif");
            eb.setThumbnail("https://cdn.discordapp.com/attachments/821102901945958440/917368562543374366/ZKkEg1G_1.jpg");
            //e.getChannel().sendMessageEmbeds(eb.build()).queue();

            TextChannel textChannel = jda.getChannelById(TextChannel.class, 591165674848780289L);

            textChannel.sendMessageEmbeds(eb.build()).queue(message -> {
                message.addReaction(yesReaction).queue();
                message.addReaction(neutralReaction).queue();
                message.addReaction(noReaction).queue();
            });


            e.reply("Suggestion made!").queue();
        }


        if(e.getModalId().equals("quote-modal")){

            String quote = e.getValue("quote-string").getAsString();
            String author = e.getValue("quote-author").getAsString();

            TextChannel textChannel = jda.getChannelById(TextChannel.class, 434930464604553218L);

            StringBuilder messageText = new StringBuilder();

            messageText.append("Submitted by **" + e.getMember().getEffectiveName() + "**\n\n");
            messageText.append("```\"" + quote + "\"```" + " - " + author);

            textChannel.sendMessage(messageText.toString()).queue();


            e.reply("Quote submitted!").queue();
        }

    }

}
