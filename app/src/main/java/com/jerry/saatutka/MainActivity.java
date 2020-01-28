package com.jerry.saatutka;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.os.AsyncTask;
import android.os.Bundle;

import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import java.util.Map;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        alustaSpinneri();
        muutaYlapalkki();

    }

    private void alustaSpinneri(){
        Spinner spinner = findViewById(R.id.kaupungit_spinner);


        // Haetaan vakioista kaupungit ja luodaan Arraylist, jossa on myös alkio "Kaikki kaupungit"
        ArrayList<String> pudotusvalikonAlkiot = new ArrayList<>();
        pudotusvalikonAlkiot.add(getString(R.string.kaikki_kaupungit));

        pudotusvalikonAlkiot.addAll(VAKIOT.KAUPUNGIT.keySet());
        // Muutetaan arraylist arrayksi, jotta saadaan se pudotusvalikkoon
        String[] kaupunkiArray = pudotusvalikonAlkiot.toArray(new String[0]);

        // Luodaan pudotusvalikolle adapteri
        ArrayAdapter<CharSequence> adapter =new ArrayAdapter<CharSequence>(this,
                android.R.layout.simple_spinner_item, kaupunkiArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        // Asetetaan toiminnallisuus, kun käyttäjä valitsee kaupungin pudotusvalikosta tai
        // pudotusvalikko luodaan.
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                String valittuKaupunki = parentView.getItemAtPosition(position).toString();

                // Tarkastetaan onko käyttäjä syöttänyt avainta
                if(VAKIOT.AVAIN.equals("")){
                    Toast.makeText(getApplicationContext(),
                            getString(R.string.avain_virhe), Toast.LENGTH_SHORT)
                            .show();
                    avaaAvaimenSyottoDialogi();
                    return;
                }

                // Haetaan rajapinnasta sää ja luodaan sääolio taustalla
                SaatilalistanHaku task = new SaatilalistanHaku();
                if(VAKIOT.KAUPUNGIT.containsKey(valittuKaupunki)){
                    // Valittu yksi kaupunki
                    task.execute(VAKIOT.KAUPUNGIT.get(valittuKaupunki));
                }
                else {
                    // Valittu kaikki kaupungit
                    Integer[] kaupunkiIDArray = VAKIOT.KAUPUNGIT.values().toArray(new Integer[0]);
                    task.execute(kaupunkiIDArray);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }

        });
    }

    private class SaatilalistanHaku extends AsyncTask<Integer, Void, ArrayList<Saatila>> {

        // Taustalla suoritettava task. Ottaa parametrinaan kaupunkien tunnisteet.
        @Override
        protected ArrayList<Saatila> doInBackground(Integer... params) {
            ArrayList<Saatila> kaupunkienSaaTilat = new ArrayList<>();
            for (int kaupunginTunniste : params){
                String kaupunginNimi = "";

                // Haetaan rajapintojen kutsuista vastaus merkkijonoihin
                String nykyinenSaa = ((new HttpRequesti()).haeKaupunginSaa(kaupunginTunniste, VAKIOT.NYKYINEN_SAA));
                String ennuste = ((new HttpRequesti()).haeKaupunginSaa(kaupunginTunniste, VAKIOT.ENNUSTE));


                // Haetaan kaupungin avain manuaalisesti
                for (Map.Entry<String, Integer> entry : VAKIOT.KAUPUNGIT.entrySet()) {
                    if (entry.getValue().equals(kaupunginTunniste)) {
                        kaupunginNimi = entry.getKey();
                    }
                }

                try {
                    JSONObject JSONennuste = new JSONObject(ennuste);
                    JSONObject JSONnykyinen = new JSONObject(nykyinenSaa);

                    // Luodaan Saatila olio ja asetetaan listaan
                    Saatila saatila = new Saatila(JSONnykyinen, JSONennuste, kaupunginNimi);
                    kaupunkienSaaTilat.add(saatila);

                }
                catch (JSONException e){
                    e.printStackTrace();
                }
            }

            // For looppi päättyi, järjestetään haettu lista aakkosjärjestykseen
            Collections.sort(kaupunkienSaaTilat, new Comparator<Saatila>() {
                @Override
                public int compare(Saatila bo1, Saatila bo2) {
                    return (bo1.haeKaupunki().compareToIgnoreCase(bo2.haeKaupunki()));
                }
            });
            // Tää palautuu onPostExecute metodille
            return kaupunkienSaaTilat;

        }

        @Override
        protected void onPostExecute(ArrayList<Saatila> saatilaLista) {
            // Saatila-oliot on luotu, kutsutaan käyttöliittymän päivittävää metodia
            paivitaKayttoliittyma(saatilaLista);
        }
    }


    // Metodi luo uuden adapterin haetuista säätila olioista.
    // Adapteri asetetaan graafiselle listalle, josta seuraa listan päivittyminen
    private void paivitaKayttoliittyma(ArrayList<Saatila> saatilaLista){
        SaatilaAdapteri adapteri = new SaatilaAdapteri(
                this,
                R.layout.saalistan_elementti,
                saatilaLista);

        ListView lista = findViewById(R.id.saaelementtien_container);

        lista.setAdapter(adapteri);
    }

    private void muutaYlapalkki(){
        if(getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        else {
            findViewById(R.id.ylapalkki).setVisibility(View.GONE);
        }
    }


    private void avaaAvaimenSyottoDialogi(){
        final AlertDialog dialogBuilder = new AlertDialog.Builder(MainActivity.this).create();
        View dialogView = View.inflate(getApplicationContext(),R.layout.avaimen_syotto_dialogi, null);
        final TextView avainkentta = dialogView.findViewById(R.id.avain_kentta);
        Button okNappi = dialogView.findViewById(R.id.avain_ok);

        okNappi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                VAKIOT.AVAIN = avainkentta.getText().toString();
                SaatilalistanHaku task = new SaatilalistanHaku();
                dialogBuilder.dismiss();
                Integer[] kaupunkiIDArray = VAKIOT.KAUPUNGIT.values().toArray(new Integer[0]);
                task.execute(kaupunkiIDArray);

            }
        });

        dialogBuilder.setView(dialogView);
        dialogBuilder.show();
    }

}
