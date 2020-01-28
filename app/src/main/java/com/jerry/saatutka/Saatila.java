package com.jerry.saatutka;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

// Saatila luokka sisältää ohjelman tiedot yksittäisen kaupungin tämänhetkisestä säästä
// sekä "VAKIOT.ENNUSTUS_AJANKOHTIA"-parametrin määräämästä ennusteiden määrästä.
class Saatila {


    private final List<Double> lampotilaLista = new ArrayList<>();
    private final List<Integer> kosteusLista = new ArrayList<>();
    private final List<Long> aikaLista = new ArrayList<>();
    private final List<String> kuvausLista = new ArrayList<>();
    private final List<Double> tuuliLista = new ArrayList<>();
    private final List<Double> sadeLista = new ArrayList<>();
    private final List<String> iconLista = new ArrayList<>();
    private final String kaupunki;


    public Saatila(JSONObject nykyinenVastaus, JSONObject ennusteVastaus, String kaupunginNimi) {
        kaupunki = kaupunginNimi;
        try{
            // Haetaan eka nykyisten havaintojen tiedot osaksi luokan muuttujia

            JSONArray weatherArray = nykyinenVastaus.getJSONArray("weather");

            JSONObject weatherObject = weatherArray.getJSONObject(0);
            kuvausLista.add(weatherObject.getString("description"));
            iconLista.add("http://openweathermap.org/img/wn/"
                    + weatherObject.getString("icon") + "@2x.png");

            JSONObject mainObject = nykyinenVastaus.getJSONObject("main");
            lampotilaLista.add(mainObject.getDouble("temp"));
            kosteusLista.add(mainObject.getInt("humidity"));

            JSONObject windObject = nykyinenVastaus.getJSONObject("wind");
            tuuliLista.add(windObject.getDouble("speed"));

            // Sademäärään lasketaan vesi- ja lumisade yhteen
            double sadeMaara = 0;

            // Sade ja lumi avainta ei löydy, jos ei ole ennustettu sadetta tai lunta
            if(nykyinenVastaus.has("rain")){
                JSONObject rain = nykyinenVastaus.getJSONObject("rain");
                if (rain.has("3h")){
                    sadeMaara += rain.getDouble("3h");
                }
                else if (rain.has("1h")){
                    sadeMaara += rain.getDouble("1h");
                }

            }
            // Lasketaan lumisade mukaan sademäärään
            if(nykyinenVastaus.has("snow")){
                JSONObject snow = nykyinenVastaus.getJSONObject("snow");
                if (snow.has("3h")){
                    sadeMaara += snow.getDouble("3h");
                }
                else if (snow.has("1h")){
                    sadeMaara += snow.getDouble("1h");
                }

            }
            sadeLista.add(sadeMaara);

            aikaLista.add(nykyinenVastaus.getLong("dt"));



            // Siirrytään hakemaan tiedot ennusteiden JSON objektista

            JSONArray lista = ennusteVastaus.getJSONArray("list");
            for (int i = 0; i < VAKIOT.ENNUSTUS_AJANKOHTIA; i++){



                JSONObject ennuste = lista.getJSONObject(i);

                long aikaleima = ennuste.getInt("dt");

                aikaLista.add(aikaleima);

                JSONObject main = ennuste.getJSONObject("main");
                lampotilaLista.add( main.getDouble("temp"));
                kosteusLista.add(main.getInt("humidity"));

                JSONObject weather = ennuste.getJSONArray("weather").getJSONObject(0);
                kuvausLista.add(weather.getString("description"));

                iconLista.add("http://openweathermap.org/img/wn/"
                        + weather.getString("icon") + "@2x.png");

                JSONObject wind = ennuste.getJSONObject("wind");
                tuuliLista.add(wind.getDouble("speed"));

                sadeMaara = 0;

                // Sade ja lumi avainta ei löydy, jos ei ole ennustettu sadetta tai lunta
                // Käyttäjälle näytetään lumi- ja vesisateen yhteismäärä
                // Tämänhetkisessä säässä on mahdollista, että kolmen tunnin havainto puuttuu,
                // mutta tunnin havainto on saatavilla. Ilmoitetaan tunnin havainto.

                if(ennuste.has("rain")){
                    JSONObject rain = ennuste.getJSONObject("rain");
                    if (rain.has("3h")){
                        sadeMaara += rain.getDouble("3h");
                    }
                    else if (rain.has("1h")){
                        sadeMaara += rain.getDouble("1h");
                    }
                }
                // Lasketaan lumisade mukaan sademäärään
                if(ennuste.has("snow")){
                    JSONObject snow = ennuste.getJSONObject("snow");
                    if (snow.has("3h")){
                        sadeMaara += snow.getDouble("3h");
                    }
                    else if (snow.has("1h")){
                        sadeMaara += snow.getDouble("1h");
                    }
                }
                sadeLista.add(sadeMaara);
            }


        }
        catch (JSONException e){
            e.printStackTrace();
        }

    }

    public String haeLampotilaCelsiuksina(int ennusteenAjankohta){
        try {
            int celsiusInt = (int) Math.round(lampotilaLista.get(ennusteenAjankohta) - 272.15);
            return String.valueOf(celsiusInt);

        }
        catch (IndexOutOfBoundsException e)
        {
            e.printStackTrace();
            return "";
        }
    }

    public int haeKosteus(int ennusteenAjankohta){
        try {
            return kosteusLista.get(ennusteenAjankohta);

        }
        catch (IndexOutOfBoundsException e)
        {
            e.printStackTrace();
            return -1;
        }
    }

    public String haeKuvaus(int ennusteenAjankohta){
        try {
            String kuvaus = kuvausLista.get(ennusteenAjankohta);
            return kuvaus.substring(0, 1).toUpperCase() + kuvaus.substring(1);

        }
        catch (IndexOutOfBoundsException e)
        {
            e.printStackTrace();
            return "";
        }
    }

    public Double haeSademaara(int ennusteenAjankohta){
        try {
            return sadeLista.get(ennusteenAjankohta);

        }
        catch (IndexOutOfBoundsException e)
        {
            e.printStackTrace();
            return -1.0;
        }
    }

    public Double haeTuuli(int ennusteenAjankohta){
        try {
            return tuuliLista.get(ennusteenAjankohta);

        }
        catch (IndexOutOfBoundsException e)
        {
            e.printStackTrace();
            return -1.0;
        }
    }

    public String haeIcon(int ennusteenAjankohta){
        try {
            return iconLista.get(ennusteenAjankohta);

        }
        catch (IndexOutOfBoundsException e)
        {
            e.printStackTrace();
            return "";
        }
    }

    public String haeKaupunki(){return kaupunki;}

    public String haeKellonaika(int ennusteenAjankohta){
        // Haetaan käyttäjälle ennusteen kellonaika, laitteen kellonajan perusteella
        // Tällä tavoin esim ulkomailla sovellusta käyttävä henkilö näkee paikallista aikaa sään
        // Hyötynä hän voi nopeasti tarkastaa toimiston viimeisen havainnon
        // ja katsoa puhelimen kellosta montako minuuttia vanha se on miettimättä aikavyöhykettä

        Calendar cal = Calendar.getInstance();
        TimeZone tz = cal.getTimeZone();

        SimpleDateFormat formaatti = new SimpleDateFormat("HH:mm");
        formaatti.setTimeZone(tz);
        try {
            return formaatti.format(new Date(aikaLista.get(ennusteenAjankohta) * 1000));

        }
        catch (IndexOutOfBoundsException e)
        {
            e.printStackTrace();
            return "";
        }

    }

    public String haePvm(int ennusteenAjankohta){
        // Palautetaan päivämäärä muotoa 1.1. käyttäjän laitteen aikavyöhykkeen perusteella.

        Calendar cal = Calendar.getInstance();
        TimeZone tz = cal.getTimeZone();

        SimpleDateFormat formaatti = new SimpleDateFormat("d.M.");
        formaatti.setTimeZone(tz);
        try {
            return formaatti.format(new Date(aikaLista.get(ennusteenAjankohta) * 1000));

        }
        catch (IndexOutOfBoundsException e)
        {
            e.printStackTrace();
            return "";
        }

    }
}