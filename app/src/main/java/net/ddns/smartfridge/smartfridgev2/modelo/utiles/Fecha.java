package net.ddns.smartfridge.smartfridgev2.modelo.utiles;

import android.content.Context;
import android.net.ParseException;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Clase con los métodos para trabajar con las fechas
 */

public class Fecha {
    private Calendar hoy;//Para crear el objeto Calendar con la fecha de hoy
    private int diasParaCaducidad=0;//Para almacenar los dias de caducidad que tiene el alimento
    private static final int MILISEGUNDOS = 86400000;//Representa los milisegundos que tiene un día

    //Método para conseguir la fecha actual
    public String fechaActual(){
        //Recogemos la fecha actual
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        Date date = new Date();
        //Lo pasamos a un string
        String fecha_actual = dateFormat.format(date);
        Log.d("fecha", "fecha actual111: " + fecha_actual);
        //Toast.makeText(this, "fecha actual: " + fecha_actual, Toast.LENGTH_SHORT).show();
        return fecha_actual;
    }
    //Método para saber una fecha a partir de unos días dados
    public String diasAFecha(int tiempo_Caducidad){
        //Instanciamos un objeto Calendar
        hoy = Calendar.getInstance();
        hoy.add(Calendar.DATE, tiempo_Caducidad);
        SimpleDateFormat dateformatter = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String f_caducidad_alimento = dateformatter.format(hoy.getTime());
        return f_caducidad_alimento;
    }

    //Método para obtener los días a partir de una fecha dada
    public int fechaDias(String fechaCalendarioMas, Context context) throws java.text.ParseException {
        Log.d("fecha", "entramos en el metodo");
        //Cogemos la fecha actual
        String dia_hoy = fechaActual();
        //Creamos el SimpleDateFormat para formatear los Strings con las fechas
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        Date fechaInicial= null; //Para almacenar la fecha de hoy
        Date fechaFinal=null;//Para almacenar la fecha de caducidad
        try {
            //Formateamos los strings con las fechas
            //Log.d("fecha", "dia hoy: " + dia_hoy);
            fechaInicial = dateFormat.parse(dia_hoy);
            //Log.d("fecha", "dia hoy 2: " + dia_hoy);
            fechaFinal=dateFormat.parse(fechaCalendarioMas);
            //Toast.makeText(context, "fechaCalendarioMas: " + fechaCalendarioMas, Toast.LENGTH_SHORT).show();
            //Hacemos la operación y lo pasamos de milisegundos a dias
            diasParaCaducidad =(int) ((fechaFinal.getTime()-fechaInicial.getTime())/MILISEGUNDOS);
        } catch (NullPointerException e){
            diasParaCaducidad = 0;
        }

        return diasParaCaducidad;
    }
}
