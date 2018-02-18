package net.ddns.smartfridge.smartfridgev2.vista.fragmentos;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.ddns.smartfridge.smartfridgev2.R;
import net.ddns.smartfridge.smartfridgev2.vista.actividades.sr.SeleccionarRecetaActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainSr extends Fragment {


    public MainSr() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_sugerir_receta, container, false);

        view.findViewById(R.id.bSeleccionarRectea).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Abrirmos el intent
                Intent i = new Intent(getActivity(), SeleccionarRecetaActivity.class);
                startActivity(i);
            }
        });

        view.findViewById(R.id.bSugerirReceta).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return view;
    }
}
