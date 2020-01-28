package com.jerry.saatutka;

import java.util.HashMap;

// Luokka pitää sisällään sovelluksen käyttämiä vakioita.
// Avain muuttujaa ei ole merkitty vakioksi, koska sitä muutetaan, jos käyttäjä syöttää avaimen
// samalla kuin suorittaa sovellusta mobiililaitteella
class VAKIOT {

    // Jos sovellukseen halutaan lisätä uusia kaupunkeja, riittää kun lisää kaupungin nimen
    // ja tunnisteen listaan
    static final HashMap<String, Integer> KAUPUNGIT = new HashMap<>();
    static {
        KAUPUNGIT.put("Helsinki", 658225);
        KAUPUNGIT.put("Jyväskylä", 655195);
        KAUPUNGIT.put("Kuopio", 650225);
        KAUPUNGIT.put("Tampere", 634964);
    }

    // TODO Syötä karttapalvelun avain tähän kenttään
    static String AVAIN = "";

    static final int PIENTEN_ELEMENTTIEN_LKM = 5;

    static final int ENNUSTUS_AJANKOHTIA = 5;

    static final int NYKYINEN_SAA = 1;
    static final int ENNUSTE = 2;
}
