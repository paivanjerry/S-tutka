package com.jerry.saatutka;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

// Luokka hoitaa sovelluksen http-pyyntöjen lähettämisen
class HttpRequesti {
    private static String URL_ALKUOSA;
    private static String URL_LOPPUOSA;

    // haeKaupunginSaa ottaa ensimmäisenä parametrina halutun kaupungin tunnisteen.
    // Toisena parametrina on kokonaisluku, joka kuvaa onko haluttu toiminto nykyisen sään vai
    // ennusteen hakeminen. Metodi palauttaa palvelimen antaman vastauksen merkkijonona.
    public String haeKaupunginSaa(int id, int haluttuSaa) {
        if(haluttuSaa == VAKIOT.ENNUSTE){
            URL_ALKUOSA = "https://api.openweathermap.org/data/2.5/forecast?id=";
            URL_LOPPUOSA = "&appid=" + VAKIOT.AVAIN;
        }
        else if (haluttuSaa == VAKIOT.NYKYINEN_SAA){
            URL_ALKUOSA = "https://api.openweathermap.org/data/2.5/weather?id=";
            URL_LOPPUOSA = "&appid=" + VAKIOT.AVAIN;
        }

        InputStream is = null;
        HttpURLConnection yhteys = null;

        try {

            yhteys = (HttpURLConnection) ( new URL(URL_ALKUOSA + id + URL_LOPPUOSA)).openConnection();
            yhteys.setRequestMethod("GET");
            yhteys.setDoInput(true);
            yhteys.setDoOutput(true);
            yhteys.connect();

            StringBuilder buffer = new StringBuilder();
            is = yhteys.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            String line;
            while (  (line = br.readLine()) != null )
                buffer.append(line).append("\r\n");

            is.close();
            yhteys.disconnect();
            return buffer.toString();

        }
        catch(Throwable t) {
            t.printStackTrace();
        }
        finally {
            try { is.close(); } catch(Throwable t) {}
            try { yhteys.disconnect(); } catch(Throwable t) {}
        }

        return null;

    }
}
