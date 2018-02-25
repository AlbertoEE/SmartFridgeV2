package net.ddns.smartfridge.smartfridgev2.vista.actividades;

import android.app.ActionBar;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

import net.ddns.smartfridge.smartfridgev2.R;
import net.ddns.smartfridge.smartfridgev2.modelo.servicios.ComprobarCaducidadIntentService;
//import net.ddns.smartfridge.smartfridgev2.modelo.servicios.RecetasIntentService;
import net.ddns.smartfridge.smartfridgev2.modelo.utiles.Dialogos;
import net.ddns.smartfridge.smartfridgev2.vista.fragmentos.MainCa;
import net.ddns.smartfridge.smartfridgev2.vista.fragmentos.MainClc;
import net.ddns.smartfridge.smartfridgev2.vista.fragmentos.MainPm;
import net.ddns.smartfridge.smartfridgev2.vista.fragmentos.MainSr;

public class InitialActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private Intent intentServicio;//Para iniciar el IntentService de caducidad y escasez
    //private Intent intentRecetas;//Para iniciar el IntentService de recetas
    private static final String NOMBRE_SERVICIO= "net.ddns.smartfridge.smartfridgev2.ComprobarCaducidadIntentService";
    private static SharedPreferences sp;//Para recoger el SP de la app

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.ca:
                    MainCa ca = new MainCa();
                    setTitle("Control de Alimentos");
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.content, ca, "CA");
                    fragmentTransaction.commit();
                    return true;
                case R.id.pm:
                    MainPm pm = new MainPm();
                    setTitle("Programar Menu");
                    FragmentTransaction fragmentTransaction2 = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction2.replace(R.id.content, pm, "PM");
                    fragmentTransaction2.commit();
                    return true;
                case R.id.clc:
                    MainClc clc = new MainClc();
                    setTitle("Crear lista de la Compra");
                    FragmentTransaction fragmentTransaction3 = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction3.replace(R.id.content, clc, "CLC");
                    fragmentTransaction3.commit();
                    return true;
                case R.id.sr:
                    MainSr sr = new MainSr();
                    setTitle("Sugerencias de recetas");
                    FragmentTransaction fragmentTransaction4 = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction4.replace(R.id.content, sr, "SR");
                    fragmentTransaction4.commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial);
        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        MainCa ca = new MainCa();
        setTitle("Control de Alimentos");
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content, ca, "CA");
        fragmentTransaction.commit();
        //Comprobamos si está activo el IntentService
        if (!servicioEjecutando()){
            //Si no está activo, lo iniciamos
            intentServicio = new Intent(this,ComprobarCaducidadIntentService.class); //serv de tipo Intent
            this.startService(intentServicio); //ctx de tipo Context
            //intentRecetas = new Intent(this, RecetasIntentService.class);//serv de tipo Intent
            //this.startService(intentRecetas);//ctx de tipo Context
            Log.d("servicio", "Arranca el servicio");
        } else {
            Log.d("servicio", "El servicio ya está ejecutándose!!!");
        }
        sp = getPreferences(Context.MODE_PRIVATE);
        //Ponemos el tutorial para el inicio
        mostrarTutorial();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    //Método para comprobar si ya está iniciado el servicio
    private boolean servicioEjecutando() {
        //Cogemos los servicios activos
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        //Los recorremos con un foreach
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            Log.d("servicio", "servicio: " + service.service.getClassName());
            //Comprobamos que en los servicios activos coincida el nombre con el servicio que queremos comprobar
            if (NOMBRE_SERVICIO.equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static SharedPreferences getSp() {
        return sp;
    }

    private void mostrarTutorial(){
        final SharedPreferences tutorialShowcases = getSharedPreferences("showcaseTutorial", MODE_PRIVATE);

        boolean run;

        run = tutorialShowcases.getBoolean("run?", true);

        if(run){//Comprobamos si ya se ha mostrado el tutorial en algún momento

            /*Declaramos todas las variables que vamos a usar
            final ViewTarget b1 = new ViewTarget(R.id.ca, this);
            final ViewTarget b2 = new ViewTarget(R.id.pm, this);
            final ViewTarget b3 = new ViewTarget(R.id.sr , this);
            final ViewTarget b4 = new ViewTarget(R.id.clc , this);*/

            //Creamos un nuevo LayoutParms para cambiar el botón de posición
            final RelativeLayout.LayoutParams lps = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lps.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            lps.addRule(RelativeLayout.CENTER_HORIZONTAL);
            // Ponemos márgenes al botón
            int margin = ((Number) (getResources().getDisplayMetrics().density * 16)).intValue();
            lps.setMargins(margin, margin, margin, margin);

            //Creamos el ShowCase
            final ShowcaseView s = new ShowcaseView.Builder(this)
                    .setTarget( new ViewTarget( ((View) findViewById(R.id.ca)) ) )
                    .setContentTitle("Control de Alimentos")
                    .setContentText("Botón para gestionar el control de los alimentos de MiNevera")
                    .hideOnTouchOutside()
                    .build();
            s.setButtonText("Siguiente");
            s.setButtonPosition(lps);
            //Comprobamos que el botón del showCase se pulsa para hacer el switch. Se va acomprobar el contador para ver si se muestra el siguiente showcas
            s.overrideButtonClick(new View.OnClickListener() {
                int contadorS = 0;

                @Override
                public void onClick(View v) {
                    contadorS++;
                    switch (contadorS) {
                        case 1:
                            s.setTarget( new ViewTarget( ((View) findViewById(R.id.sr)) ) );
                            s.setContentTitle("Sugerir recetas");
                            s.setContentText("Botón para gestionar todas las recetas con sus filtros");
                            break;

                        case 2:
                            s.setTarget( new ViewTarget( ((View) findViewById(R.id.clc)) )  );
                            s.setContentTitle("Programar lista de la compra");
                            s.setContentText("Botón para gestionar las listas de la compra");
                            break;

                        case 3:
                            s.setTarget( new ViewTarget( ((View) findViewById(R.id.pm)) )  );
                            s.setContentTitle("Programar Menu");
                            s.setContentText("Botón para gestionar la programación de los menús (En desarrollo)");
                            break;
                        case 4:

                            break;
                        case 5:
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
