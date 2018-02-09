package net.ddns.smartfridge.smartfridgev2.modelo.adaptadores;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import net.ddns.smartfridge.smartfridgev2.R;

import java.util.ArrayList;

import cn.refactor.library.SmoothCheckBox;

/**
 * Created by Alberto on 07/02/2018.
 */

public class CustomArrayAdapter extends ArrayAdapter<String> {
    private ArrayList<String> alimentos;
    private ArrayList<String> alimentosSeleccionados;

    public CustomArrayAdapter(Context context, ArrayList<String> alimentos){
        super(context, R.layout.fila_alimentos_sugeridos, alimentos);
        this.alimentos = alimentos;
        alimentosSeleccionados = new ArrayList<>();
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final String alimento = alimentos.get(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.fila_alimentos_sugeridos, parent, false);
        }

        TextView tvAlimentoSugerido = convertView.findViewById(R.id.textViewAlimentoSugerido);
        SmoothCheckBox scb = convertView.findViewById(R.id.smoothCheckBox);

        tvAlimentoSugerido.setText(alimento);
        scb.setOnCheckedChangeListener(new SmoothCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SmoothCheckBox smoothCheckBox, boolean b) {
                if(b){
                    alimentosSeleccionados.add(alimentos.get(position));
                    Log.d("RETRASO", "onCheckedChanged: TRUE " + position);
                } else {
                    alimentosSeleccionados.remove(alimento);
                    Log.d("RETRASO", "onCheckedChanged: FALSO " + position);
                }
            }
        });

        return convertView;
    }

    public ArrayList<String> getNuevaLista(){
        return alimentosSeleccionados;
    }

}
