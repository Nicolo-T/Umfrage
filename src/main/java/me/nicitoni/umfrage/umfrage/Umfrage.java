package me.nicitoni.umfrage.umfrage;

import me.nicitoni.umfrage.main.Main;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Umfrage {

    public String name;
    public int time;
    public int no;
    public int yes;
    public String id;

    public Umfrage() {

    }

    public static Umfrage getUmfrageByName(String id) {
        Umfrage u = null;
        if(!(Main.umfrageArrayList.isEmpty())) {
            for (Umfrage umfrage : Main.umfrageArrayList) {
                if(umfrage.getId().equalsIgnoreCase(id)) {
                    u =  Main.umfrageArrayList.get(Main.umfrageArrayList.indexOf(umfrage));
                }
            }
        }
        return u;
    }

    public void start() {

    }

    public void stop(Umfrage umfrage) {
        List<Umfrage> delete = new ArrayList<>();

        int index = Main.umfrageArrayList.indexOf(umfrage);
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("- Auswertung "+umfrage.getName()+" -");
        eb.setFooter("<Coded by NT>");
        eb.setThumbnail("https://cdn.pixabay.com/photo/2013/11/28/11/28/hands-220163__340.jpg");
        eb.setColor(Main.hauptFarbe);
        eb.addBlankField(false);
        eb.addField("ANZAHL JA", String.valueOf(umfrage.getYes()-1), true);
        eb.addBlankField(false);
        eb.addField("ANZAHL NEIN", String.valueOf(umfrage.getNo()-1), true);
        Main.api.getTextChannelsByName("umfrage", true).get(0).sendMessage("Die Umfrage " + umfrage.getName() + " wurde beendet!").submit();
        if(umfrage.getYes() > umfrage.getNo()) {
            eb.addBlankField(false);
            eb.addField("RESULTAT: ", "Ja wurde abgestimmt", true);
            Main.api.getTextChannelsByName("umfrage", true).get(0).sendMessage(eb.build()).submit();
            delete.add(umfrage);
        } else if(umfrage.getNo() > umfrage.getYes()) {
            eb.addBlankField(false);
            eb.addField("RESULTAT: ", "Nein wurde abgestimmt", true);
            Main.api.getTextChannelsByName("umfrage", true).get(0).sendMessage(eb.build()).submit();
            delete.add(umfrage);
        } else {
            eb.addBlankField(false);
            eb.addField("RESULTAT: ", "GLEICHSTAND", true);
            Main.api.getTextChannelsByName("umfrage", true).get(0).sendMessage(eb.build()).submit();
            delete.add(umfrage);
        }

        delete.forEach(umfrage2 -> {
            Main.umfrageArrayList.remove(Main.umfrageArrayList.indexOf(umfrage2));
        });

        if(!(delete == null || delete.isEmpty())) {
            delete.clear();
        }
    }



    public String getName() {
        return name;
    }

    public int getTime() {
        return time;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getYes() {
        return yes;
    }

    public int getNo() {
        return no;
    }

    public void setYes(int yes) {
        this.yes = yes;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public void addYes() {
        yes++;
    }

    public void addNo() {
        no++;
    }

    public void removeYes() {
        yes--;
    }

    public void removeNo() {
        no--;
    }

    public static void startTimer() {
        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                List<Umfrage> delete = new ArrayList<>();
                if(!(Main.umfrageArrayList == null)) {
                    Main.umfrageArrayList.forEach(umfrage -> {
                        System.out.println(umfrage.getName() + " - " + umfrage.getYes() + " " + umfrage.getNo() + " " + umfrage.getId());
                        if(umfrage.getTime() > 0) {
                            umfrage.setTime(umfrage.getTime()-1);
                            System.out.println(umfrage.getName() + " - "+ umfrage.getTime());
                        } else {
                            umfrage.stop(umfrage);

                        }
                    });
                }


                delete.forEach(umfrage -> {
                    Main.umfrageArrayList.remove(Main.umfrageArrayList.indexOf(umfrage));
                });

                if(!(delete == null || delete.isEmpty())) {
                    delete.clear();
                }

            }
        }, 0, 20*60*60);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


}
