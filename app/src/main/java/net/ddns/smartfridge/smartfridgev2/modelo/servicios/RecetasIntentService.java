package net.ddns.smartfridge.smartfridgev2.modelo.servicios;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.util.Log;

import net.ddns.smartfridge.smartfridgev2.modelo.basico.Receta;
import net.ddns.smartfridge.smartfridgev2.persistencia.MySQL.MySQLHelper;
import net.ddns.smartfridge.smartfridgev2.vista.fragmentos.MainSr;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class RecetasIntentService extends IntentService {
    private ArrayList<Receta> recetas;//Para almacenar todas las recetas de la bbdd
    private MySQLHelper myHelper;//Para acceder a la bbdd
    private static final long MINUTO=60000;//Milisegundos que hay en 1 minuto
    private static final int DELAY = 1000;//Delay usado para arrancar el service
    private MainSr mainSr;

    public RecetasIntentService() {
        super("RecetasIntentService");
        recetas= new ArrayList<>();
        myHelper = new MySQLHelper();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                try {
                    //Abrimos la conexión a la bbdd
                    myHelper.abrirConexion();
                    //Recogemos las recetas una a una
                    recetas = myHelper.recogerRecetas();
                    mainSr.crearAdapter(recetas);
                } catch (SQLException e) {
                    Log.d("SQL", "Error al conectarse a la bbdd: " + e.getErrorCode());
                } catch (ClassNotFoundException e) {
                    Log.d("SQL", "Error al establecer la conexión: " + e.getMessage());
                }
                try {
                    myHelper.cerrarConexion();
                } catch (SQLException e) {
                    Log.d("SQL", "Error al cerrar la bbdd");
                }
                for(int i = 0;i<recetas.size(); i++){
                    Log.d("intentService", "Receta en intentService: " + recetas.get(i).getTituloReceta());
                }
            }
        };
        Timer timer = new Timer();
        timer.schedule(timerTask,DELAY,MINUTO);
    }

    public ArrayList<Receta> getRecetas(){
        return this.recetas;
    }

    public void setMainSr(MainSr mainSr){
        this.mainSr = mainSr;
    }
}

