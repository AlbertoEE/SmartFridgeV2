package net.ddns.smartfridge.smartfridgev2.vista.fragmentos;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

import net.ddns.smartfridge.smartfridgev2.R;
import net.ddns.smartfridge.smartfridgev2.vista.actividades.ca.IdentificarAlimentoActivity;
import net.ddns.smartfridge.smartfridgev2.vista.actividades.ca.MiNeveraActivity;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainCa extends Fragment {

    public MainCa() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_ca, container, false);
        // Inflate the layout for this fragment
        view.findViewById(R.id.ibIdentificar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), IdentificarAlimentoActivity.class);
                startActivity(i);
            }
        });

        view.findViewById(R.id.ibNevera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), MiNeveraActivity.class);
                startActivity(i);
            }
        });
        //mostrarTutorial(view);
        return view;
    }

    private void mostrarTutorialCa(View v) {
        final SharedPreferences tutorialShowcases = getActivity().getSharedPreferences("showcaseTutorial", MODE_PRIVATE);

        boolean run;

        run = tutorialShowcases.getBoolean("run?", true);

        if (run) {//Comprobamos si ya se ha mostrado el tutorial en algún momento

            //Declaramos todas las variables que vamos a usar
            //final ViewTarget b1 = new ViewTarget(R.id.ibIdentificar, getActivity());
            //final ViewTarget b2 = new ViewTarget(R.id.ibNevera, getActivity());

            /*Creamos un nuevo LayoutParms para cambiar el botón de posición
            final RelativeLayout.LayoutParams lps = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lps.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            lps.addRule(RelativeLayout.CENTER_HORIZONTAL);
            // Ponemos márgenes al botón
            int margin = ((Number) (getResources().getDisplayMetrics().density * 16)).intValue();
            lps.setMargins(margin, margin, margin, margin);*/

            //Creamos el ShowCase
            final ShowcaseView s = new ShowcaseView.Builder(getActivity())
                    .setTarget(new ViewTarget(((View) v.findViewById(R.id.ibIdentificar))))
                    .setContentTitle("Identificar nuevos alimentos")
                    .setContentText("Pulsa para identificar alimentos que vayas a incluir como nuevos en MiNevera")
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
                            s.setTarget(new ViewTarget(((View) v.findViewById(R.id.ibNevera))));
                            s.setContentTitle("Ver MiNevera");
                            s.setContentText("Pulsa para ver los alimentos guardados en MiNevera");
                            break;

                        case 2:
                            //Cambiamos la variable en el sharedPreferences para que no se vuelva a mostrar el tutorial
                            SharedPreferences.Editor tutorialShowcasesEdit = tutorialShowcases.edit();
                            tutorialShowcasesEdit.putBoolean("run?", false);
                            tutorialShowcasesEdit.apply();
                            s.hide();
                            break;
                    }
                }
            });
        }
    }
}
