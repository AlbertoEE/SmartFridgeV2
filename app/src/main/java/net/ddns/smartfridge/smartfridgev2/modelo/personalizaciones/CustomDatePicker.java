package net.ddns.smartfridge.smartfridgev2.modelo.personalizaciones;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import net.ddns.smartfridge.smartfridgev2.R;
import net.ddns.smartfridge.smartfridgev2.vista.actividades.ca.CaducidadAlimento;

import java.text.ParseException;
import java.util.Calendar;

/**
 * Clase para crear un calendario personalizado
 */
public class CustomDatePicker {
    /**
     * Variable que representa una instancia del calendario
     */
    public final Calendar calendar = Calendar.getInstance();

    //Obtenemos el mes, dia y año actual
    private final int mes = calendar.get(Calendar.MONTH) + 1; //Sumamos uno porque empiezan en el mes 0
    private final int dia = calendar.get(Calendar.DAY_OF_MONTH);
    private final int anio = calendar.get(Calendar.YEAR);

    private Activity activity;//Representa una Activity
    private Context c;//Para indicar el contexto
    private CaducidadAlimento ca;
    private RelativeLayout relativeLayout;

    /**
     * Constructor
     *
     * @param context  el contexto donde se va a ejecutar
     * @param activity la activity donde se va a ejecutar
     */
    public CustomDatePicker(Context context, Activity activity){
        this.c = context;
        this.activity = activity;
        this.ca = (CaducidadAlimento) activity;
    }

    /**
     * Método para instanciar al calendario
     *
     * @throws ParseException lanza la exception cuando no se puede hacer el parse a string
     */
    public void obtenerFecha() throws ParseException {
        //Creamos el diálogo
        DatePickerDialog datePickerDialog = new DatePickerDialog(c, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                //Sumamos 1 al mes porque empieza en 0
                month = month + 1;
                //hacemos el set de la fecha inicial y final en la clase caducidadAlimento
                ca.setFechas(fechasConverter(dia, mes, anio), fechasConverter(dayOfMonth, month, year));
                comprobarFecha();
                //Ponemos la bandera a 1 porque hemos elegido la fecha via calendario
                ca.setControlDragAndDrop(1);

            }
        }, anio, mes, dia);
        //Fecha mínima
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        //Mostramos el diálogo
        datePickerDialog.show();
        //Asignamos un listener al cancelar
        datePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                relativeLayout = ca.findViewById(R.id.relativeLayout);

                //Si la drop zone tiene hijos asignados significa que ya hay fecha en el drag and
                //drop por lo tanto nos quedamos con la del drag and drop, si no tiene hijos significa
                //que al cancelar no tiene ni drag and drop ni calendario por lo tanto la bandera
                //se pone a 0
                if(relativeLayout.getChildCount() != 0){
                    ca.setControlDragAndDrop(-1);
                }else{
                    ca.setControlDragAndDrop(0);
                }
            }
        });
    }

    /**
     * Método para convertir el dia, mes y año de int a String
     *
     * @param dia int que representa el número del día del mes
     * @param mes int que representa el número de mes del año
     * @param anio int que representa el año
     * @return String con la fecha completa
     */
    private String fechasConverter(int dia, int mes, int anio){
        String fecha;
        fecha = (dia < 10)? String.valueOf("0" + dia) : String.valueOf(dia);
        fecha += "-" + ((mes < 10)? String.valueOf("0" + mes) : String.valueOf(mes));
        fecha += "-" + String.valueOf(anio);
        return fecha;
    }

    /**
     * Comprobamos si la bandera está afirmando que hay una fecha elegida en el drag and drop
     * y si la hay la desasignamos de la zona del drag and drop debido a que lo comprobamos justo
     * antes de asignar una fecha desde el calendario.
     *
     */
    private void comprobarFecha(){
        //Este método recoge los layout del drag and drop y limpia la zona del drop y asigna el hijo
        //de la drop zone a su layout original
        if (ca.getControlDragAndDrop() == -1){
            View children[];

            RelativeLayout relativeLayout = (RelativeLayout) ca.findViewById(R.id.relativeLayout);
            View central = relativeLayout.getChildAt(0);
            relativeLayout.removeView(central);

            LinearLayout linearLayout = (LinearLayout) ca.findViewById(R.id.linearLayout);
            View ejemplo = linearLayout.getChildAt(0);
            LinearLayout.LayoutParams ll = (LinearLayout.LayoutParams) ejemplo.getLayoutParams();

            central.setLayoutParams(ll);
            linearLayout.addView(central);

            children = new View[7];
            for (int i=0; i < 7; i++){
                children[i] = linearLayout.getChildAt(i);
            }
            int x = 0;
            while(x != 7) {
                for (int i = 1; i < 8; i++) {
                    for (View item : children) {
                        if (Integer.parseInt(c.getResources().getResourceEntryName(item.getId()).substring(5)) == i) {
                            linearLayout.removeView(item);
                            linearLayout.addView(item);
                            x++;
                        }
                    }
                }
            }
        }
    }
}
