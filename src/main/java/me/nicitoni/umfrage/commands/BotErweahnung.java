package me.nicitoni.umfrage.commands;

import me.nicitoni.umfrage.main.Main;
import me.nicitoni.umfrage.umfrage.Umfrage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.ShutdownEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class BotErweahnung extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
        if(event.getAuthor().isBot() || event.getAuthor().isFake()) return;
        if(event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            if(event.getMessage().getContentRaw().equalsIgnoreCase("Umfrage")) {
                event.getMessage().delete().submit();
                EmbedBuilder eb = new EmbedBuilder();
                eb.setTitle("- Help v1.0 -");
                eb.setThumbnail("https://cdn.pixabay.com/photo/2017/07/10/23/43/question-mark-2492009__340.jpg");
                eb.setColor(Main.hauptFarbe);
                eb.addBlankField(false);
                eb.addField("INFORMATON", "!umfrage <create / ping / clear / stop>",false);
                eb.addBlankField(false);
                eb.addField("INFORMATON", "!umfrage create <Zeit in Min> <Frage>", false);
                eb.addBlankField(false);
                eb.addField("INFORMATON", "!umfrage stop <Nachrichten ID>", false);
                eb.addBlankField(false);
                eb.addField("INFORMATON", "!umfrage clear", false);
                eb.addBlankField(false);
                eb.addField("INFORMATON", "!umfrage ping", false);
                eb.setFooter("Bot erstellt von NT");

                event.getChannel().sendMessage(eb.build()).queue(message -> {
                    Timer t = new Timer();
                    t.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            message.delete().submit();
                        }
                    }, 20*60*10);
                });
            }

        }
    }

    @Override
    public void onReady(@Nonnull ReadyEvent event) {
        event.getJDA().getTextChannelsByName("debuginfo", true).get(0).sendMessage("READY! ist die scheise").submit();
    }

    @Override
    public void onMessageReactionAdd(@Nonnull MessageReactionAddEvent event) {
        if(!(Main.umfrageArrayList.isEmpty())) {
            System.out.println(Umfrage.getUmfrageByName(event.getMessageId()));
            if(event.getReactionEmote().getEmote().getName().equalsIgnoreCase("yes")) {
                System.out.println(event.getReaction() + " ");
                Main.umfrageArrayList.forEach(umfrage -> {
                    if(umfrage.getId().equalsIgnoreCase(event.getMessageId())) {
                        umfrage.addYes();
                    }
                });
                System.out.println(Umfrage.getUmfrageByName(event.getMessageId()).getYes());
            } else if(event.getReactionEmote().getEmote().getName().equalsIgnoreCase("no")){
                Main.umfrageArrayList.forEach(umfrage -> {
                    if(umfrage.getId().equalsIgnoreCase(event.getMessageId())) {
                        umfrage.addNo();
                    }
                });

            } else {
                return;
            }
        }


    }

    @Override
    public void onShutdown(@Nonnull ShutdownEvent event) {
        Main.api.getGuilds().forEach(guild -> {
            guild.getTextChannelsByName("debuginfo", true).get(0).sendMessage("Server nicht mehr Ready").submit();
        });
    }

    @Override
    public void onMessageReactionRemove(@Nonnull MessageReactionRemoveEvent event) {
        if(!(Main.umfrageArrayList.isEmpty())) {
            if(event.getReactionEmote().getEmote().getName().equalsIgnoreCase("yes")) {
                System.out.println("YA REMOVEM");
                Main.umfrageArrayList.forEach(umfrage -> {
                    if(umfrage.getId().equalsIgnoreCase(event.getMessageId())) {
                        umfrage.removeYes();
                    }
                });
            } else if(event.getReactionEmote().getEmote().getName().equalsIgnoreCase("no")){
                System.out.println(event.getReactionEmote().getEmote().getName());
                System.out.println("NO REMOVEM");
                Main.umfrageArrayList.forEach(umfrage -> {
                    if(umfrage.getId().equalsIgnoreCase(event.getMessageId())) {
                        umfrage.removeNo();
                    }
                });
            } else {
                return;
            }
        }


    }
}
