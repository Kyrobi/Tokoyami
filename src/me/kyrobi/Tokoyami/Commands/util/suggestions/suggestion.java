package me.kyrobi.Tokoyami.Commands.util.suggestions;

import me.kyrobi.Tokoyami.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.util.*;
import java.util.List;

public class suggestion extends ListenerAdapter {


    String disclaimer =
            "Some suggestions are not plausible due to limitations or how it affects the server. " +
                    "This also includes modifying a plugin that would require the original developer. Here are some prerequisites before making a suggestion: \n\n" +

                    "**Prerequisite 1**: Redundancy\n"+
                    "Has your suggestion been suggested before? If so, don’t post it. Please use the search function on Discord before making a duplicate suggestion. " +
                    "If you truly believe this suggestion is worth a re-consideration, talk about it in general or off-topic and gauge opinions of other people before making one.\n\n"+

                    "**Prerequisite 2**: Seriousness\n"+
                    "Your suggestion needs to have careful thought behind them. We want to take each suggestion into consideration. " +
                    "Having troll / lackluster suggestions makes that hard to happen. Joke suggestions will be removed.\n\n"+

                    "**Prerequisite 3**: Follow the EULA\n"+
                    "Despite existing p2w perks for VIP, adding more p2w content needs to be avoided. All suggestions must follow Mojang’s EULA. Any suggestions that break the EULA will be removed.\n\n"+

                    "EULA summary\n"+
                    "`You may not provide perks that would give a specific group/person more gameplay advantage than other players that are otherwise unobtainable through vanilla means. A perk that benefits everyone is okay.`\n\n"+

                    "Type **I agree** if you have read the disclaimer and agree to its conditions.";



    String SuggestionSummaryMessage = "**Give a summary of your suggestion** (140 characters minimum)";
    String SuggestionPositiveMessage = "**Why do you think this suggestion should be added?** (140 characters minimum)";
    String SuggestionNegativeMessage = "**Why do you think this suggestion should NOT be added?** (140 characters minimum)";

    //Map the user's Discord ID to their responses
    Map<Long, FieldObject> buffer = new HashMap<>();

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
            long authorID = e.getAuthor().getIdLong();
            String userMessage = e.getMessage().getContentRaw();


            /*
            If the suggestion does not contain the template, we deny it
             */

//            if(!userMessage.contains("**Summary of suggestion:**") || !userMessage.contains("**Explanation on why this suggestion should be implemented:**") || !userMessage.contains("**Explanation on why this suggestion should NOT be implemented:**")){
//                e.getChannel().sendMessage("Please use the suggestion template!").queue();
//                return;
//            }


            for(int i = 1; i < args.length; i++){
                suggestion.append(args[i] + " ");
            }

            if(suggestion.length() > 1024){
                e.getChannel().sendMessage("Your suggestion is too damn long (" + suggestion.length() + " characters)! Please limit it to 1024 characters.").queue();
                return;
            }

            if(suggestion.length() < 500){
                e.getChannel().sendMessage("Your suggestion is too short (" + suggestion.length() + " characters) - minimum of 500 characters. Please give more detail.").queue();
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
            eb.addField("Submitter", Objects.requireNonNull(e.getMember()).getNickname() + " | " + e.getAuthor().getAsTag(), false);
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


//    @Override
//    public void onPrivateMessageReceived(PrivateMessageReceivedEvent ev){
//
//        if(ev.getAuthor().isBot()){
//            return;
//        }
//
//        String[] args = ev.getMessage().getContentRaw().split(" ");
//
//        if(args[0].equalsIgnoreCase(Main.prefix + "suggest")){
//
//            ev.getChannel().sendMessage(disclaimer)
//                    .queue(message -> waiter.waitForEvent( // Setup Wait action once message was send
//                            PrivateMessageReceivedEvent.class,
//                                    e -> {
//                                        if(!e.getChannel().getId().equals(ev.getChannel().getId())) // Check that channel is the same
//                                        {
//                                            return false;
//                                        }
//
//                                        return e.getAuthor().getIdLong() == ev.getAuthor().getIdLong(); // Check for same author
//                                    },
//                                    e -> {
//                                        String authorMessage = e.getMessage().getContentRaw();
//                                        ev.getChannel().sendMessage(authorMessage).queue();
//                                    },
//                                    1, TimeUnit.MINUTES,
//                                    () -> {
//                                        ev.getChannel().sendMessage("You didn't respond in time!").queue();
//                                    }
//                            )
//                    );
//
//            ev.getChannel().sendMessage("Next message").queue();
//
////            waiter.waitForEvent(PrivateMessageReceivedEvent.class, event -> event.getAuthor().equals(ev.getAuthor()) && event.getChannel().equals(ev.getChannel()), event ->{
////                //event.getChannel().sendMessage(event.getMessage()).queue();
////                //event.getChannel().getType() == ChannelType.PRIVATE
////
////                System.out.println("sent in dm !");
////                event.getChannel().sendMessage("You entered " + event.getMessage()).queue();
////                event.getChannel().sendMessage(event.getMessage().getContentRaw()).queue();
////                //System.out.println("sent in dm ");
////            });
//
//        }
//    }
}
