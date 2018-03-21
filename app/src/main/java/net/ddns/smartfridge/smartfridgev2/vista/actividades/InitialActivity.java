package net.ddns.smartfridge.smartfridgev2.vista.actividades;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.widget.Toast;

import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

import net.ddns.smartfridge.smartfridgev2.R;
import net.ddns.smartfridge.smartfridgev2.modelo.servicios.ComprobarCaducidadIntentService;
import net.ddns.smartfridge.smartfridgev2.vista.fragmentos.MainCa;
import net.ddns.smartfridge.smartfridgev2.vista.fragmentos.MainClc;
import net.ddns.smartfridge.smartfridgev2.vista.fragmentos.MainPm;
import net.ddns.smartfridge.smartfridgev2.vista.fragmentos.MainSr;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Activity inicial de la aplicación
 */
public class InitialActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private Intent intentServicio;//Para iniciar el IntentService de caducidad y escasez
    private static final String NOMBRE_SERVICIO= "net.ddns.smartfridge.smartfridgev2.ComprobarCaducidadIntentService";
    private static SharedPreferences sp;//Para recoger el SP de la app
    //Atributos de la clase que hacen referencia a los fragmentos para llamar a los metodos de los tutoriales de cada fragmento
    private MainCa mainCa;
    private MainSr mainSr;
    private MainClc mainClc;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.ca:
                    mainCa = new MainCa();
                    setTitle(getString(R.string.ca));
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.content, mainCa, "CA");
                    fragmentTransaction.commit();
                    return true;
                case R.id.pm:
                    MainPm pm = new MainPm();
                    setTitle(getString(R.string.pm));
                    FragmentTransaction fragmentTransaction2 = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction2.replace(R.id.content, pm, "PM");
                    fragmentTransaction2.commit();
                    return true;
                case R.id.clc:
                    mainClc = new MainClc();
                    setTitle(getString(R.string.clc));
                    FragmentTransaction fragmentTransaction3 = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction3.replace(R.id.content, mainClc, "CLC");
                    fragmentTransaction3.commit();
                    return true;
                case R.id.sr:
                    mainSr = new MainSr();
                    setTitle(getString(R.string.sr));
                    FragmentTransaction fragmentTransaction4 = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction4.replace(R.id.content, mainSr, "SR");
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
        mainCa = new MainCa();
        setTitle(getString(R.string.ca));
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content, mainCa, "CA");
        fragmentTransaction.commit();
        //Comprobamos si está activo el IntentService
        if (!servicioEjecutando()){
            //Si no está activo, lo iniciamos
            intentServicio = new Intent(this,ComprobarCaducidadIntentService.class); //serv de tipo Intent
            this.startService(intentServicio); //ctx de tipo Context
            Log.d("servicio", "Arranca el servicio");
        } else {
            Log.d("servicio", "El servicio ya está ejecutándose!!!");
        }
        sp = getPreferences(Context.MODE_PRIVATE);
        //Ponemos el tutorial para el inicio
        Log.d("lñlñ", "onCreate: " + mainCa);
        checkInternetConnection();
        mostrarTutorial();
    }

    private void checkInternetConnection() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null)
            Toast.makeText(this, getString(R.string.no_internet), Toast.LENGTH_LONG).show();
        else {
            try {
                Toast.makeText(this, getString(R.string.si_internet), Toast.LENGTH_LONG).show();
                String parameters = ""; //
                URL url = new URL("http://smartFridge.ddns.net" + parameters);
                executeReq(url);
            }
            catch(Exception e){
                Log.d("error", "" + e.getMessage());
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * Método para comprobar si ya está iniciado el servicio
     * @return true o false en función de si se está ejecutando el service
     */
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

    private void executeReq(URL urlObject) throws IOException {
        HttpURLConnection conn = null;

        conn = (HttpURLConnection) urlObject.openConnection();
        conn.setReadTimeout(100000); //Milliseconds
        conn.setConnectTimeout(150000); //Milliseconds
        conn.setRequestMethod(getString(R.string.get));
        conn.setDoInput(true);

        // Start connect
        conn.connect();
        String response = (conn.getInputStream()).toString();
        Log.d("Response:", response);
    }

    private static String convertStreamToString(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line + "\n");
        }
        is.close();
        return sb.toString();
    }

    /**
     * Gets sp.
     *
     * @return the sp
     */
    public static SharedPreferences getSp() {
        return sp;
    }

    /**
     * Método para mostrar el tutorial al usuario
     */
    private void mostrarTutorial(){
        final SharedPreferences tutorialShowcases = getSharedPreferences(getString(R.string.tutorialSP), MODE_PRIVATE);

        boolean run;

        run = tutorialShowcases.getBoolean(getString(R.string.tutorial6), true);

        if(run){//Comprobamos si ya se ha mostrado el tutorial en algún momento
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
                    .setContentTitle(getString(R.string.ca))
                    .setContentText(getString(R.string.ca_t))
                    .hideOnTouchOutside()
                    .build();
            s.setButtonText(getString(R.string.siguiente));
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
                            s.setContentTitle(getString(R.string.sr));
                            s.setContentText(getString(R.string.sr_t));
                            break;

                        case 2:
                            s.setTarget( new ViewTarget( ((View) findViewById(R.id.clc)) )  );
                            s.setContentTitle(getString(R.string.clc));
                            s.setContentText(getString(R.string.clc_t));
                            break;

                        case 3:
                            s.setTarget( new ViewTarget( ((View) findViewById(R.id.pm)) )  );
                            s.setContentTitle(getString(R.string.pm));
                            s.setContentText(getString(R.string.pm_c));
                            break;
                        case 4:
                            s.hide();
                            break;
                        case 5:
                            //Cambiamos la variable en el sharedPreferences para que no se vuelva a mostrar el tutorial
                            SharedPreferences.Editor tutorialShowcasesEdit = tutorialShowcases.edit();
                            tutorialShowcasesEdit.putBoolean(getString(R.string.tutorial6), false);
                            tutorialShowcasesEdit.apply();
                            break;
                    }
                }
            });
        }
    }
}
