package net.ddns.smartfridge.smartfridgev2.vista.fragmentos;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

import net.ddns.smartfridge.smartfridgev2.R;
import net.ddns.smartfridge.smartfridgev2.vista.actividades.clc.NuevaListaActivity;
import net.ddns.smartfridge.smartfridgev2.vista.actividades.clc.SugerenciaDeAlimentoActivity;
import net.ddns.smartfridge.smartfridgev2.vista.actividades.clc.TodasListasActivity;

import static android.content.Context.MODE_PRIVATE;

/**
 * Fragment para la parte de control lista de la compra
 */
public class MainClc extends Fragment {


    /**
     * Constructor
     */
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
            Intent intent = new Intent(getActivity(), TodasListasActivity.class);
            startActivity(intent);
            }
        });

        view.findViewById(R.id.ibNuevaLista).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), NuevaListaActivity.class);
                startActivity(intent);
            }
        });
        mostrarTutorialCa(view);
        return view;
    }

    /**
     * Método para mostrar el tutorial
     *
     * @param v the v
     */
    public void mostrarTutorialCa(View v) {
        final SharedPreferences tutorialShowcases = getActivity().getSharedPreferences("showcaseTutorial", MODE_PRIVATE);

        boolean run;

        run = tutorialShowcases.getBoolean("runMainClc", true);

        if (run) {//Comprobamos si ya se ha mostrado el tutorial en algún momento

            //Creamos el ShowCase
            final ShowcaseView s = new ShowcaseView.Builder(getActivity())
                    .setTarget(new ViewTarget(((View) v.findViewById(R.id.ibVerLista))))
                    .setContentTitle("Ver todas las listas")
                    .setContentText("Pulsa para ver todas las listas de la compra que hay guardadas")
                    .hideOnTouchOutside()
                    .build();
            s.setButtonText("Siguiente");
            //s.setButtonPosition(lps);
            //Comprobamos que el botón del showCase se pulsa para hacer el switch. Se va acomprobar el contador para ver si se muestra el siguiente showcas
            s.overrideButtonClick(new View.OnClickListener() {
                int contadorS = 0;

                @Override
                public void onClick(View v) {
                    contadorS++;
                    switch (contadorS) {
                        case 1:
                            s.setTarget(new ViewTarget(((View) v.findViewById(R.id.ibNuevaLista))));
                            s.setContentTitle("Crear Nueva Lista");
                            s.setContentText("Pulsa para crear una nueva lista de la compra.");
                            break;
                        case 2:
                            //Cambiamos la variable en el sharedPreferences para que no se vuelva a mostrar el tutorial
                            SharedPreferences.Editor tutorialShowcasesEdit = tutorialShowcases.edit();
                            tutorialShowcasesEdit.putBoolean("runMainClc", false);
                            tutorialShowcasesEdit.apply();
                            s.hide();
                            break;
                    }
                }
            });
        }
    }

}
