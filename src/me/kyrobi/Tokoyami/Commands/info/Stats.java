package me.kyrobi.Tokoyami.Commands.info;

import me.kyrobi.Tokoyami.Main;
import me.kyrobi.Tokoyami.objects.SystemInfo;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

import static me.kyrobi.Tokoyami.Main.jda;
import static me.kyrobi.Tokoyami.objects.SystemInfo.getSystemInformation;

public class Stats extends ListenerAdapter {

    @Override //Overrides the super class
    public void onGuildMessageReceived(GuildMessageReceivedEvent e){
        String[] args = e.getMessage().getContentRaw().split(" "); // split up every argument since every argument has a space

        if(args[0].equalsIgnoreCase(Main.prefix + "stats")){

            // I stole the to compute the format the time from the internet. I don't have the divine intellect

            RuntimeMXBean bean = ManagementFactory.getRuntimeMXBean(); // Fetches the JVM uptime

            long different = bean.getUptime(); // long upTime = bean.getUptime(); Time in ms

            long secondsInMilli = 1000;
            long minutesInMilli = secondsInMilli * 60;
            long hoursInMilli = minutesInMilli * 60;
            long daysInMilli = hoursInMilli * 24;

            long days = different / daysInMilli;
            different = different % daysInMilli;

            long hours = different / hoursInMilli;
            different = different % hoursInMilli;

            long minutes = different / minutesInMilli;
            different = different % minutesInMilli;

            long seconds = different / secondsInMilli;

            String uptime = days + "d " + hours + "h " + minutes + "m " + seconds + "s ";
            long serverCount = jda.getGuildCache().size();

            SystemInfo sysinfo = new SystemInfo();

            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle("Stats", null);
            eb.setColor(Color.red);
            eb.setColor(new Color(0xF40C0C));
            eb.setColor(new Color(255, 0, 54));
            // eb.setDescription("Text");
            eb.addField("Name", "Mio Mama#4889", true);
            eb.addField("Developer", "Kyrobi#9982", true);
            eb.addField("Servers", "`" + String.valueOf(serverCount) + "`", true);
            eb.addField("Command Prefix", "`m!`", true);
            eb.addField("Process Load", sysinfo.getProcessCPULoad() + "%", true);
            eb.addField("System Load", sysinfo.getSystemCPULoad() + "%", true);
            eb.addField("Memory", String.valueOf(getSystemInformation()), true);
            eb.addField("Uptime", uptime, true);
            eb.addField("", "", true);
            // eb.addBlankField(true);
            //eb.setAuthor("name", null, "https://github.com/zekroTJA/DiscordBot/blob/master/.websrc/zekroBot_Logo_-_round_small.png");
            // eb.setFooter("Text", "https://github.com/zekroTJA/DiscordBot/blob/master/.websrc/zekroBot_Logo_-_round_small.png");
            eb.setImage("https://cdn.discordapp.com/attachments/845435268974116864/917359478964375572/MioFlap.gif");
            eb.setThumbnail("https://cdn.discordapp.com/attachments/821102901945958440/917368562543374366/ZKkEg1G_1.jpg");
            e.getChannel().sendMessageEmbeds(eb.build()).queue();
        }
    }
}
