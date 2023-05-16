package me.kyrobi.Tokoyami.Commands.util.suggestions;

import me.kyrobi.Tokoyami.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.*;
import java.util.List;

public class suggestion extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent e){

        if(e.getName().equals("suggestion")){
            TextInput summary = TextInput.create("suggestion-summary", "summary", TextInputStyle.SHORT)
                    .setMinLength(20)
                    .setRequired(true)
                    .setPlaceholder("What is your suggestion?")
                    .build();

            TextInput description = TextInput.create("suggestion-description", "This is a good idea because:", TextInputStyle.PARAGRAPH)
                    .setMinLength(150)
                    .setRequired(true)
                    .setPlaceholder("Give a description on why your suggestion should be implemented")
                    .build();

            TextInput description_against = TextInput.create("suggestion-description-against", "This might not be a good idea because:", TextInputStyle.PARAGRAPH)
                    .setMinLength(150)
                    .setRequired(true)
                    .setPlaceholder("Give an opposing description on why your suggestion should NOT be implement")
                    .build();


            Modal modal = Modal.create("suggestion-modal", "Suggestion")
                    .addActionRows(ActionRow.of(summary), ActionRow.of(description), ActionRow.of(description_against))
                    .build();

            e.replyModal(modal).queue();
        }
    }
}
