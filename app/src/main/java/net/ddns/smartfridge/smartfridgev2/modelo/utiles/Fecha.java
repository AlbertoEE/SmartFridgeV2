package net.ddns.smartfridge.smartfridgev2.modelo.utiles;

import android.content.Context;
import android.net.ParseException;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Clase con los métodos para trabajar con las fechas
 */
public class Fecha {
    private Calendar hoy;//Para crear el objeto Calendar con la fecha de hoy
    private int diasParaCaducidad=0;//Para almacenar los dias de caducidad que tiene el alimento
    private static final int MILISEGUNDOS = 86400000;//Representa los milisegundos que tiene un día

    /**
     * Método para conseguir la fecha actual sin las horas
     *
     * @return String con la fecha actual en formato String
     */
//Método para conseguir la fecha actual
    public String fechaActual(){
        //Recogemos la fecha actual
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        Date date = new Date();
        //Lo pasamos a un string
        String fecha_actual = dateFormat.format(date);
        Log.d("fecha", "fecha actual111: " + fecha_actual);
        return fecha_actual;
    }

    /**
     * Método para conseguir la fecha actual con las horas
     *
     * @return String con la fecha actual con las horas
     */
//Método para conseguir la fecha actual
    public String fechaActualCompleta(){
        //Recogemos la fecha actual
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        //Lo pasamos a un string
        String fecha_actual = dateFormat.format(date);
        Log.d("fecha", "fecha actual111: " + fecha_actual);
        return fecha_actual;
    }

    /**
     * Método para pasar de formato completo a formato corto de una fecha
     *
     * @param fechaLarga String con la fecha en formato con horas
     * @return String con la fecha en formato sin horas
     */
//Método para pasar de formato completo a formato corto de una fecha
    public String fechaCorta(String fechaLarga){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        Date date = new Date();
        try {
            date = dateFormat.parse(fechaLarga);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        String fechaCorta = dateFormat2.format(date);
        Log.d("fecha2", "fecha actual111: " + fechaCorta);
        return fechaCorta;
    }

    /**
     * Método para saber una fecha a partir de unos días dados
     *
     * @param tiempo_Caducidad int con los dias que faltan para la caducidad
     * @return String con la fecha en la que caduca el alimento
     */
//Método para saber una fecha a partir de unos días dados
    public String diasAFecha(int tiempo_Caducidad){
        //Instanciamos un objeto Calendar
        hoy = Calendar.getInstance();
        hoy.add(Calendar.DATE, tiempo_Caducidad);
        SimpleDateFormat dateformatter = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String f_caducidad_alimento = dateformatter.format(hoy.getTime());
        return f_caducidad_alimento;
    }

    /**
     * Método para obtener los días a partir de una fecha dada
     *
     * @param fechaCalendarioMas String con la fecha elegida a través del calendario
     * @param context            contexto donde se ejecuta el método
     * @return the int          int con los días que faltan para que caduque un alimento
     * @throws ParseException   cuando no se puede cambiar el formato de la fecha
     */
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
