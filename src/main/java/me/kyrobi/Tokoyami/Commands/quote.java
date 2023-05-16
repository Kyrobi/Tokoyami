package me.kyrobi.Tokoyami.Commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import org.jetbrains.annotations.NotNull;

import static me.kyrobi.Tokoyami.Main.userQuoteTimer;

public class quote extends ListenerAdapter {

    private final long MILLIS_IN_ONE_DAY = 86_400_000;

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent e){


        if(e.getName().equals("quote")){

            // If user in the list, check their last time
            if(userQuoteTimer.containsKey(e.getMember().getIdLong())){
                long currentTime = System.currentTimeMillis();
                long previousTime = userQuoteTimer.get(e.getMember().getIdLong());

                long difference = currentTime - previousTime;
                if(difference < MILLIS_IN_ONE_DAY){
                    e.reply("You can only submit 1 quote every 24-hours").queue();
                    return;
                }
            }

            // If user isn't in the list, we add it and then allow them to make quote
            else {
                long time = System.currentTimeMillis();
                userQuoteTimer.put(e.getMember().getIdLong(), time);
            }

            TextInput quote = TextInput.create("quote-string", "Quote:", TextInputStyle.PARAGRAPH)
                    .setMinLength(5)
                    .setRequired(true)
                    .setPlaceholder("What is the quote? (Don't include the quotation marks)")
                    .build();

            TextInput author = TextInput.create("quote-author", "Person who made the quote:", TextInputStyle.PARAGRAPH)
                    .setMinLength(3)
                    .setRequired(true)
                    .setPlaceholder("Who said this quote? (Minecraft username)")
                    .build();


            Modal modal = Modal.create("quote-modal", "Submit Quote")
                    .addActionRows(ActionRow.of(quote), ActionRow.of(author))
                    .build();

            e.replyModal(modal).queue();
        }
    }
}
