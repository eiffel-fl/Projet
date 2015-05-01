package projet;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author francis
 */
public class Message {

    static int nbMessages;
    int NUM_MESSAGE;

    String pseudo;
    String message;
    String heure;

    public Message(String psd, String msg) {
        pseudo = psd;
        message = msg;

        nbMessages++;
        NUM_MESSAGE = nbMessages;

        Date now = new Date();
        
        Locale locale = new Locale("fr", "FR");
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", locale);
       
        heure = sdf.format(now);
    }

    @Override
    public String toString() {
        return "[" + heure + "] " + "<b>" + pseudo + "</b> : " + message;
    }
}
