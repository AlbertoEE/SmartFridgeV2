package net.ddns.smartfridge.smartfridgev2.vista;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
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
            }
        }, anio, mes, dia);
        //date= format.parse(diaRecogido+mesRecogido+anioRecogido);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    private String fechasConverter(int dia, int mes, int anio){
        String fecha;

        fecha = (dia < 10)? String.valueOf("0" + dia) : String.valueOf(dia);
        fecha += "-" + ((mes < 10)? String.valueOf("0" + mes) : String.valueOf(mes));
        fecha += "-" + String.valueOf(anio);
        Toast.makeText(c, fecha, Toast.LENGTH_SHORT).show();
        return fecha;
    }
}
