package me.nicitoni.umfrage.main;

import me.nicitoni.umfrage.commands.BotErweahnung;
import me.nicitoni.umfrage.commands.UmfrageCMD;
import me.nicitoni.umfrage.umfrage.Umfrage;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import javax.security.auth.login.LoginException;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {

    public static JDA api;
    public static Color hauptFarbe = new Color((int)(Math.random() * 0x1000000));
    public static ArrayList<Umfrage> umfrageArrayList = new ArrayList<>();

    public static void main(String[] args) throws LoginException {
        api = JDABuilder.createDefault("").build();

        api.addEventListener(new BotErweahnung());
        api.addEventListener(new UmfrageCMD());

        Umfrage.startTimer();
    }


}
