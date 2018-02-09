package net.ddns.smartfridge.smartfridgev2.modelo.servicios;

import android.app.IntentService;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.Toast;

import net.ddns.smartfridge.smartfridgev2.modelo.basico.Alimento;
import net.ddns.smartfridge.smartfridgev2.modelo.utiles.Dialogos;
import net.ddns.smartfridge.smartfridgev2.modelo.utiles.Fecha;
import net.ddns.smartfridge.smartfridgev2.persistencia.gestores.AlimentoDB;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static java.lang.Thread.sleep;


/**
 * IntentService para comprobar periódicamente la caducidad de los alimentos
 */
public class ComprobarCaducidadIntentService extends IntentService {
    private Cursor cursor;//Para almacenar la consulta a la bbdd
    private String fechaCaducidad;//La fecha de caducidad recuperada de la bbdd
    private Alimento alimento;//Para construir el objeto Alimento y trabajar con él
    private static final int DIAS_CADUCIDAD=2;//La diferencia máxima que puede haber entre la fecha actual y la de caducidad, para que salte así la notificación o el número mínimo
    //de unidades que debe haber de un producto
    private int diasParaCaducidad;//Vamos a almacenar los días que falten para que caduque el alimento
    private Fecha fecha;//Para usar el método que calcula los días que hay de diferencia entre dos fechas
    private static Bitmap bm;//Para construir el Bitmap a partir de los datos de la bbdd
    private Dialogos dialogos;//Para crear las notificaciones
    //private static final long OCHO_HORAS=28800000;//Milisegundos que hay en 8 horas
    private static final long OCHO_HORAS=120000;//Milisegundos que hay en 2 minutos
    private static final long MINUTO=60000;//Milisegundos que hay en 1 minuto
    private static final int DELAY = 1000;//Delay usado para distintas partes del código
    private int unidades;//Representa el número de unidades de cada producto almacenado en MiNevera
    private int posicionCursor;//Para indicar la posición del elemento en el cursor de la bbdd

    public ComprobarCaducidadIntentService() {
        super("ComprobarCaducidadIntentService");
        dialogos = new Dialogos(this);
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        //Hacemos la conexión a la bbddd
        final AlimentoDB alimentoDB = new AlimentoDB(this);
        fecha = new Fecha();

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                //Hacemos el while(true) para que siempre esté en ejecución
                //while(true){
                    //Recogemos todos los alimentos y los almacenamos en el cursor
                    cursor = alimentoDB.getAlimentos();
                    //Recorremos el cursor
                    if (cursor.moveToFirst()) {
                        //Recorremos el cursor hasta que no haya más registros
                        do {
                            //Recogemos la fecha de caducidad del alimento de la bbdd
                            fechaCaducidad = cursor.getString(5);
                            Log.d("servicio", "fecha de caducidad: " + fechaCaducidad);
                            byte[] byteArrayFoto;
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            byteArrayFoto = cursor.getBlob(6);
                            try {
                                bm = BitmapFactory.decodeByteArray(byteArrayFoto, 0, byteArrayFoto.length);
                                bm.compress(Bitmap.CompressFormat.PNG, 100, stream);
                            } catch (NullPointerException e){
                                //No hay imagen en la bbdd
                            }
                            //Calculamos los días de diferencia
                            try{
                                diasParaCaducidad = fecha.fechaDias(fechaCaducidad, getApplicationContext());
                                if(diasParaCaducidad<=0){
                                    //Lanzamos la notificación de alimento caducado
                                    alimento = new Alimento(cursor.getInt(0),
                                            cursor.getString(1),
                                            cursor.getInt(2),
                                            cursor.getInt(3),
                                            cursor.getString(4),
                                            cursor.getString(5),
                                            null);
                                    posicionCursor = cursor.getPosition();
                                    dialogos.enviarNotificacionCaducado(alimento, getApplicationContext(), posicionCursor);
                                }
                            } catch (ParseException e){
                                //Lanzamos la notificación de alimento caducado
                                alimento = new Alimento(cursor.getInt(0),
                                        cursor.getString(1),
                                        cursor.getInt(2),
                                        cursor.getInt(3),
                                        cursor.getString(4),
                                        cursor.getString(5),
                                        null);
                                posicionCursor = cursor.getPosition();
                                dialogos.enviarNotificacionCaducado(alimento, getApplicationContext(), posicionCursor);
                            }
                            Log.d("servicio", "dias para caducidad: " + diasParaCaducidad);
                            //Comprobamos si quedan <2 días para que caduque. Si es así, se lanzará la notificación
                            if ((diasParaCaducidad<=DIAS_CADUCIDAD) && (diasParaCaducidad > 0)) {
                                //Creamos el objeto alimento
                                alimento = new Alimento(cursor.getInt(0),
                                        cursor.getString(1),
                                        cursor.getInt(2),
                                        cursor.getInt(3),
                                        cursor.getString(4),
                                        cursor.getString(5), null);
                                //Lanzamos la notificación
                                posicionCursor = cursor.getPosition();
                                dialogos.enviarNotificacionProximaCaducidad(alimento, getApplicationContext(), posicionCursor);
                                Log.d("servicio", "Faltan: " + diasParaCaducidad + " días para que caduque.");
                            }
                            try {
                                sleep(DELAY);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            comprobarCantidad(cursor);
                        } while(cursor.moveToNext());
                    }
                }
         //   }
        };



/*

        //Hacemos el while(true) para que siempre esté en ejecución
        while(true){
            //Recogemos todos los alimentos y los almacenamos en el cursor
            cursor = alimentoDB.getAlimentos();
            //Recorremos el cursor
            if (cursor.moveToFirst()) {
                //Recorremos el cursor hasta que no haya más registros
                do {
                    //Recogemos la fecha de caducidad del alimento de la bbdd
                    fechaCaducidad = cursor.getString(5);
                    Log.d("servicio", "fecha de caducidad: " + fechaCaducidad);
                    byte[] byteArrayFoto;
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    byteArrayFoto = cursor.getBlob(6);
                    try {
                        bm = BitmapFactory.decodeByteArray(byteArrayFoto, 0, byteArrayFoto.length);
                        bm.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    } catch (NullPointerException e){
                        //No hay imagen en la bbdd
                    }
                    //Calculamos los días de diferencia
                    try{
                        diasParaCaducidad = fecha.fechaDias(fechaCaducidad, this);
                    } catch (ParseException e){
                        //Lanzamos la notificación de alimento caducado
                        alimento = new Alimento(cursor.getInt(0),
                                cursor.getString(1),
                                cursor.getInt(2),
                                cursor.getInt(3),
                                cursor.getString(4),
                                cursor.getString(5),
                                null);
                        dialogos.enviarNotificacionCaducado(alimento, getApplicationContext());
                    }
                    Log.d("servicio", "dias para caducidad: " + diasParaCaducidad);
                    //Comprobamos si quedan <2 días para que caduque. Si es así, se lanzará la notificación
                    if (diasParaCaducidad<=DIAS_CADUCIDAD) {
                        //Creamos el objeto alimento
                        alimento = new Alimento(cursor.getInt(0),
                                cursor.getString(1),
                                cursor.getInt(2),
                                cursor.getInt(3),
                                cursor.getString(4),
                                cursor.getString(5), null);
                        //Lanzamos la notificación
                        Log.d("servicio", "Faltan: " + diasParaCaducidad + " días para que caduque.");
                    }
                    try {
                        sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } while(cursor.moveToNext());
            }
        }*/
        Timer timer = new Timer();
        timer.schedule(timerTask,DELAY,MINUTO);
    }

    public static Bitmap getBm() {
        return bm;
    }

    //Método para comprobar si hay algún alimento con una cantidad <2 y enviar la notificación
    public void comprobarCantidad(Cursor cursor){
        //Almacenamos en la variable el número de unidades de cada elemento
        unidades = cursor.getInt(2);
        if (unidades < DIAS_CADUCIDAD){
            //Creamos el objeto alimento
            alimento = new Alimento(cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getInt(2),
                    cursor.getInt(3),
                    cursor.getString(4),
                    cursor.getString(5), null);
            posicionCursor = cursor.getPosition();
            Log.d("servicio", "posicion: " + posicionCursor);
            //Lanzamos la notificación
            dialogos.enviarNotificacionProximaEscasez(alimento, getApplicationContext(), posicionCursor);
            Log.d("servicio", "cantidad: " + unidades);
        }
    }
}
