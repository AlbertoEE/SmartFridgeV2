package net.ddns.smartfridge.smartfridgev2.vista.fragmentos;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.ddns.smartfridge.smartfridgev2.R;
import net.ddns.smartfridge.smartfridgev2.vista.actividades.clc.NuevaLista;
import net.ddns.smartfridge.smartfridgev2.vista.actividades.clc.SugerenciaDeAlimentsoActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainClc extends Fragment {


    public MainClc() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_crear_lista_compra, container, false);

        view.findViewById(R.id.ibVerLista).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            //pon codigo aqui si quieres que pase algo!
            Intent intent = new Intent(getActivity(), SugerenciaDeAlimentsoActivity.class);
            startActivity(intent);
            }
        });

        view.findViewById(R.id.ibNuevaLista).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), NuevaLista.class);
                startActivity(intent);
            }
        });

        return view;
    }

}
