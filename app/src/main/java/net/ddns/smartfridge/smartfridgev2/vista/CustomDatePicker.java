package net.ddns.smartfridge.smartfridgev2.vista;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import net.ddns.smartfridge.smartfridgev2.R;
import net.ddns.smartfridge.smartfridgev2.modelo.escuchadores.CustomOnDragListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Alberto on 14/01/2018.
 */

public class CustomDatePicker {
    public final Calendar calendar = Calendar.getInstance();
    private final int mes = calendar.get(Calendar.MONTH) + 1;
    private final int dia = calendar.get(Calendar.DAY_OF_MONTH);
    private final int anio = calendar.get(Calendar.YEAR);
    private Activity clase;
    private Context c;
    private CaducidadAlimento ca;

    public CustomDatePicker(Context context, Activity clase){
        c = context;
        this.clase = clase;
    }

    public void obtenerFecha() throws ParseException {
        DatePickerDialog datePickerDialog = new DatePickerDialog(c, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                month = month + 1;
                ca = (CaducidadAlimento) clase;
                ca.setFechas(fechasConverter(dia, mes, anio), fechasConverter(dayOfMonth, month, year));
                comprobarFecha();
            }
        }, anio, mes, dia);
        //date= format.parse(diaRecogido+mesRecogido+anioRecogido);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
        datePickerDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                ca.setControlDragAndDrop(0);
            }
        });
    }

    private String fechasConverter(int dia, int mes, int anio){
        String fecha;

        fecha = (dia < 10)? String.valueOf("0" + dia) : String.valueOf(dia);
        fecha += "-" + ((mes < 10)? String.valueOf("0" + mes) : String.valueOf(mes));
        fecha += "-" + String.valueOf(anio);
        Toast.makeText(c, fecha, Toast.LENGTH_SHORT).show();
        return fecha;
    }

    private void comprobarFecha(){
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
            ca.setControlDragAndDrop(1);
        }
    }
}
