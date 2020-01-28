package com.jerry.saatutka;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.squareup.picasso.Picasso;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Objects;

class SaatilaAdapteri extends ArrayAdapter<Saatila> {

    private final Context mContext;
    private final int mResource;
    private static class ViewHolder {
        TextView kaupunki;
        TextView kuvaus;
        TextView pvm;
        TextView kellonaika;
        TextView lampotila;
        TextView tuuli;
        TextView kosteus;
        TextView sade;
        ImageView kuvake;

    }
    public SaatilaAdapteri(Context context, int resource, ArrayList<Saatila> itemit){

        super(context, resource, itemit);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        // Tän metodin avulla luodaan graafinen kaupungin näkymä, kun listaa rullataan

        Saatila saatila = Objects.requireNonNull(getItem(position));
        ViewHolder holder;


        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);
            holder= new ViewHolder();

            convertView.setTag(holder);

            holder.kaupunki = convertView.findViewById(R.id.kaupunki);
            holder.kuvaus = convertView.findViewById(R.id.kuvaus);
            holder.pvm = convertView.findViewById(R.id.pvm);
            holder.kellonaika = convertView.findViewById(R.id.kellonaika);
            holder.lampotila = convertView.findViewById(R.id.lampotila);
            holder.tuuli = convertView.findViewById(R.id.tuuli);
            holder.kosteus = convertView.findViewById(R.id.kosteus);
            holder.sade = convertView.findViewById(R.id.sade);
            holder.kuvake = convertView.findViewById(R.id.kuvake);
        }
        else{
            holder = (ViewHolder) convertView.getTag();


        }

        // Asetetaan ylemmän elementin tiedot
        holder.kuvaus.setText(saatila.haeKuvaus(0));
        holder.lampotila.setText(saatila.haeLampotilaCelsiuksina(0) + " °C");

        Double tuuli = saatila.haeTuuli(0);
        DecimalFormat df0 = new DecimalFormat("#");
        DecimalFormat df1 = new DecimalFormat("#.#");
        holder.tuuli.setText( mContext.getString(R.string.tuuli) + " " + df1.format(tuuli) + " m/s");

        holder.kosteus.setText( mContext.getString(R.string.kosteus) + " "
                + saatila.haeKosteus(0) + " %");

        Double sademaara = saatila.haeSademaara(0);
        holder.sade.setText(mContext.getString(R.string.sade) + " " + df0.format(sademaara) + " mm");

        String icon = saatila.haeIcon(0);
        if (!icon.equals("")){
            Picasso.get()
                    .load(saatila.haeIcon(0))
                    .resize(200,200).into(holder.kuvake);
        }

        holder.kaupunki.setText(saatila.haeKaupunki());
        holder.kellonaika.setText(saatila.haeKellonaika(0));
        holder.pvm.setText(saatila.haePvm(0));



        // Asetetaan pienempien elementtien tiedot
        // Otetaan for looppiin pystyssä olevat ennustuksen elementit (5kpl)
        for (int i = 1; i <= VAKIOT.PIENTEN_ELEMENTTIEN_LKM; i++){

            // Ennusteiden elementit on nimetty kaavalla "pysty1", "pysty2"...
            // Niiden jälkeläisten tunnisteilla ei ole väliä toistorakenteen kannalta
            // Indeksiä käytetään tunnistamaan näkymän tyyppi (ylin=kellonaika...)
            int resID = mContext.getResources().getIdentifier("pysty"+i,
                    "id", mContext.getPackageName());
            LinearLayout pieniElementti = convertView.findViewById(resID);
            LinearLayout toinenLineaari = (LinearLayout) pieniElementti.getChildAt(0);

            TextView kellonaika = (TextView) toinenLineaari.getChildAt(0);
            ImageView kuvake = (ImageView) toinenLineaari.getChildAt(1);
            TextView lampotila = (TextView) toinenLineaari.getChildAt(2);

            LinearLayout sininenPohja = (LinearLayout) toinenLineaari.getChildAt(3);
            TextView tuuliView = (TextView) sininenPohja.getChildAt(0);
            TextView kosteusView = (TextView) sininenPohja.getChildAt(1);
            TextView sademaaraView = (TextView) sininenPohja.getChildAt(2);


            kellonaika.setText(saatila.haeKellonaika(i));
            icon = saatila.haeIcon(i);
            if (!icon.equals("")){
                Picasso.get()
                        .load(saatila.haeIcon(i))
                        .resize(100,100).into(kuvake);
            }

            lampotila.setText(saatila.haeLampotilaCelsiuksina(i) + " °C");

            tuuli = saatila.haeTuuli(i);
            tuuliView.setText(df1.format(tuuli) + " m/s");

            kosteusView.setText( saatila.haeKosteus(i) + " %");

            sademaara = saatila.haeSademaara(i);
            sademaaraView.setText(df0.format(sademaara) + " mm");

        }

        return convertView;
    }

}
