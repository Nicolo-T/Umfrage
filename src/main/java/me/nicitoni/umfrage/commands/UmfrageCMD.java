package me.nicitoni.umfrage.commands;

import me.nicitoni.umfrage.main.Main;
import me.nicitoni.umfrage.umfrage.Umfrage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

public class UmfrageCMD extends ListenerAdapter {

    String id = "";
    boolean success = false;

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
        id = " ";
        if(event.getAuthor().isBot() || event.getAuthor().isFake()) return;
        if(!(event.getMessage().getContentRaw().startsWith("!umfrage"))) return;
        if(event.getChannel().getName().startsWith("umfrage")) {
            String[] args = event.getMessage().getContentRaw().replaceAll("!umfrage ", "").split(" ");
            if(args.length != 0) {
                String name = "";
                int time = 0;
                if(args[0].equalsIgnoreCase("create")) {
                    try { time = Integer.parseInt(args[1]); } catch (NumberFormatException nfe) { event.getAuthor().openPrivateChannel().queue(privateChannel -> {
                        privateChannel.sendMessage("Bitte gib eine gültige Zahl ein").submit();
                    }); return;}

                    for (int i = 0; i < args.length; i++) {
                        if(i >= 2)  {
                            name += args[i] + " ";
                        }
                    }

                    event.getMessage().delete().submit();
                    int addMinuteTime = 5;
                    Date targetTime = new Date(); //now
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(targetTime);
                    cal.add(Calendar.MINUTE, Integer.parseInt(String.valueOf(Math.round(time))));

                    SimpleDateFormat dtf = new SimpleDateFormat("dd. MMMM, HH:mm");
                    targetTime = cal.getTime();

                    EmbedBuilder abstimmung = new EmbedBuilder();
                    abstimmung.setColor(new Color(2, 180, 73));
                    abstimmung.setTitle("- COMMUNITY ABSTIMMUNG -");

                    abstimmung.addField("", "Name: "+ name, false);
                    abstimmung.addField("Abstimmung wird ausgewertet am ", dtf.format(cal.getTime()), false);
                    abstimmung.setFooter("*Coded by NT*");
                    abstimmung.setThumbnail("https://cdn.pixabay.com/photo/2013/11/28/11/28/hands-220163__340.jpg");


                    double finalTime = time;
                    String finalName = name;

                    event.getChannel().sendMessage(abstimmung.build()).queue(message -> {
                        message.addReaction(event.getGuild().getEmotesByName("yes", true).get(0)).queue();
                        message.addReaction(event.getGuild().getEmotesByName("no", true).get(0)).queue();

                        Umfrage u = new Umfrage();
                        u.setName(finalName);
                        u.setTime(Integer.valueOf((int) Math.round(finalTime)));
                        u.setId(message.getId());

                        Main.umfrageArrayList.add(u);

                        Timer t = new Timer();

                        t.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                message.delete().submit();
                            }
                        }, Integer.valueOf((int) finalTime)*1000*60);
                    });




                } else if(args[0].equalsIgnoreCase("stop")) {
                    // !umfrage stop <messageid>
                    if(args.length == 2) {
                        String id = args[1];
                        Main.umfrageArrayList.forEach(umfrage -> {
                            if(umfrage.getId().equalsIgnoreCase(id)) {
                                success = true;
                                umfrage.stop(umfrage);

                            }
                        });
                    if(!(success)) {
                        event.getAuthor().openPrivateChannel().queue(privateChannel -> {
                            privateChannel.sendMessage("Bitte gib eine Nachrichten ID ein");
                        });
                    } else {
                        return;
                    }

                    }
                } else if(args[0].equalsIgnoreCase("ping")) {
                    event.getChannel().sendMessage("@everyone Schau dir doch mal die neue Umfrage an und stimm ab!").queue(message -> {
                        event.getMessage().delete().submit();
                    });
                } else if(args[0].equalsIgnoreCase("clear")) {
                    OffsetDateTime twoWeeksAgo = OffsetDateTime.now().minus(2, ChronoUnit.WEEKS);
                    List<Message> messages = event.getChannel().getHistory().retrievePast(50).complete();
                    messages.removeIf(m -> m.getTimeCreated().isBefore(twoWeeksAgo));
                    if (messages.isEmpty())
                    {
                        return;
                    }
                    event.getChannel().purgeMessages(messages);

                    event.getMessage().delete().submit();
                }
            } else {
                event.getAuthor().openPrivateChannel().queue(privateChannel -> {
                    privateChannel.sendMessage("Bitte gib mehr <Argumente> an").submit();
                });
            }

        }
    }
}
