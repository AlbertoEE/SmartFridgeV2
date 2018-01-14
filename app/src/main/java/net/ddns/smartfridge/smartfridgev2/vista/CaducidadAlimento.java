package net.ddns.smartfridge.smartfridgev2.vista;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.aigestudio.wheelpicker.WheelPicker;

import net.ddns.smartfridge.smartfridgev2.R;
import net.ddns.smartfridge.smartfridgev2.modelo.Alimento;
import net.ddns.smartfridge.smartfridgev2.modelo.Alimento_Codigo;
import net.ddns.smartfridge.smartfridgev2.modelo.CustomDatePicker;
import net.ddns.smartfridge.smartfridgev2.modelo.Dialogos;
import net.ddns.smartfridge.smartfridgev2.modelo.Fecha;
import net.ddns.smartfridge.smartfridgev2.modelo.escuchadores.CustomOnDragListener;
import net.ddns.smartfridge.smartfridgev2.modelo.escuchadores.CustomOnLongClickListener;
import net.ddns.smartfridge.smartfridgev2.persistencia.AlimentoDB;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class CaducidadAlimento extends AppCompatActivity {
    public static final int MAXUDS = 50;//Número máximo de uds del WheelPicker
    private int unidadesWheel;//Unidades del Wheel Picker
    private Alimento_Codigo ac;//Para almacenar el objeto que recojamos el ConfirmarAlimentoActivity
    private int tiempo_Caducidad;//Para almacenar los días de caducidad
    private AlimentoDB adb;//Instancia del Gestor de BD de Alimentos
    private CustomDatePicker customDatePicker;
    private String fecha_inicial;//Para asignar la fecha actual
    private String fecha_final;//Para asignar la fecha de caducidad a través del calendario
    private int controlDragAndDrop = 0;//Para determinar si la selección de la caducidad se ha hecho por un medio u otro
    private Alimento al;//Para construir el objeto de tipo alimento que se almacenará en la bbdd

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = getIntent();
        customDatePicker = new CustomDatePicker(this, this);
        ac = ConfirmarAlimentoActivity.getAlimento();
        setContentView(R.layout.activity_caducidad_alimento);
        ponerImagenes();
        ImageView dd = (ImageView)findViewById(R.id.ivDropZone);
        dd.setImageBitmap(ac.getImagen());
        cargarDragAndDrop();
        WheelPicker wheelPicker = (WheelPicker) findViewById(R.id.wheelUds);
        wheel(wheelPicker);
        adb = new AlimentoDB(this);
    }

    public void setTiempo_Caducidad(int tiempo_Caducidad){
        this.tiempo_Caducidad = tiempo_Caducidad;
    }

    private void cargarDragAndDrop(){
        findViewById(R.id.ivCad1).setOnLongClickListener(new CustomOnLongClickListener(1));
        findViewById(R.id.ivCad2).setOnLongClickListener(new CustomOnLongClickListener(2));
        findViewById(R.id.ivCad3).setOnLongClickListener(new CustomOnLongClickListener(3));
        findViewById(R.id.ivCad4).setOnLongClickListener(new CustomOnLongClickListener(4));
        findViewById(R.id.ivCad5).setOnLongClickListener(new CustomOnLongClickListener(5));
        findViewById(R.id.ivCad6).setOnLongClickListener(new CustomOnLongClickListener(6));
        findViewById(R.id.ivCad7).setOnLongClickListener(new CustomOnLongClickListener(7));
        findViewById(R.id.relativeLayout).setOnDragListener(new CustomOnDragListener((ImageView) findViewById(R.id.ivDropZone),
                (LinearLayout) findViewById(R.id.linearLayout), this, this));
        //findViewById(R.id.linearLayout).setOnDragListener(new CustomOnDragListener2(this));
    }

    //Método para dar las características al WheelPicker
    public void wheel(WheelPicker wheelPicker){
        //final int itemSel;//Para el item seleccionado
        //Asignamos datos al WheelPicker
        List<Integer> unidades = new ArrayList<>();
        for (int k = 1; k <= MAXUDS; k++)
            unidades.add(k);
        wheelPicker.setData(unidades);
        //Asignamos los items que van a estar visibles
        wheelPicker.setVisibleItemCount(2);
        //Le ponemos las mismas dimensiones a todos los elementos
        wheelPicker.setSameWidth(true);
        //Indicamos que al inciarse esté apuntando a un elemento, en este caso, el primero
        wheelPicker.setSelectedItemPosition(0);
        //Iniciamos la variable a 1, ya que empezará en el primer elemento, que tendrá valor 1
        unidadesWheel = 1;
        /*Para poner color de fondo
        wheelPicker.setBackgroundColor(getResources().getColor(R.color.viewfinder_laser));*/
        wheelPicker.setOnItemSelectedListener(new WheelPicker.OnItemSelectedListener() {
            @Override
            public void onItemSelected(WheelPicker picker, Object data, int position) {
                int itemSel = picker.getCurrentItemPosition();
                //Las uds van a ser la posición del wheel picker + 1
                unidadesWheel = itemSel + 1;
                //Log.d("uds", "uds: " + unidadesWheel);
                //Log.d("XEXU", String.valueOf(tiempo_Caducidad));
            }
        });
    }

    //Metodo que mostrará un dialog con la caducidad y las uds seleccionads
    public void confirmarCaducidad(View v){
        Dialogos dialogos = new Dialogos(this,this);

        //Visualizamos el contenido de la bbdd
        Cursor c = adb.getAlimentos();
        if (c.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros
            do {
                int id = c.getInt(0);
                String nombre= c.getString(1);
                int cantidad = c.getInt(2);
                int caducidad = c.getInt(3);
                String f_hoy = c.getString(4);
                String f_cad = c.getString(5);
                Log.d("tag", "Mi Nevera: " + nombre + ", " + cantidad + ", " + caducidad + ", "+ f_hoy + ", " + f_cad);
            } while(c.moveToNext());
        }

        Fecha fecha = new Fecha();
        //Convertimos en String la fecha del día de hoy
        String fecha_actual = fecha.fechaActual();
        //Verificamos si se ha seleccionado la caducidad por drag and drop o por el calendario
        //controlDragAndDrop=1;
        if (controlDragAndDrop==-1){
            //Significa que se ha seleccionado la caducidad por medio del drag and drop
            String fecha_caducidad_alimento = fecha.diasAFecha(tiempo_Caducidad);
            //Creamos el objeto Alimento
            al = new Alimento(ac.getNomAlimento(), unidadesWheel, tiempo_Caducidad, fecha_actual, fecha_caducidad_alimento, ac.getImagen());
            //Mostramos el dialog con los datos
            dialogos.dialogCaducidad(unidadesWheel, tiempo_Caducidad, al);
        } else if (controlDragAndDrop==1){
            //Significa que se ha seleccionado la caducidad por medio del calendario
            //Metemos en la variable los dias que faltan para la caducidad
            int diasCaducidad = fecha.fechaDias(fecha_final);
            //Toast.makeText(this, "dias para caducidad: " + diasCaducidad, Toast.LENGTH_LONG).show();
            //Creamos el objeto Alimento
            al = new Alimento(ac.getNomAlimento(), unidadesWheel, diasCaducidad, fecha_actual, fecha_final, ac.getImagen());
            //Mostramos el dialog con los datos
            dialogos.dialogCaducidad(unidadesWheel, diasCaducidad, al);
        } else if (controlDragAndDrop==0){
            //Programar
            dialogos.dialogNoCaducidad();
        }
        Toast.makeText(this, String.valueOf(controlDragAndDrop), Toast.LENGTH_SHORT).show();
    }

    public void botonMas(View view){
        try {
            customDatePicker.obtenerFecha();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void setFechas(String fecha_incial, String fecha_final){
        this.fecha_inicial = fecha_incial;
        this.fecha_final = fecha_final;
    }

    public int getControlDragAndDrop() {
        return controlDragAndDrop;
    }

    public void setControlDragAndDrop(int controlDragAndDrop) {
        this.controlDragAndDrop = controlDragAndDrop;
    }

    //Método para poner las imágenes a los botones del drag and drop
    public void ponerImagenes(){
        Drawable elemento;//Para almacenar los elementos drawable
        Bitmap bitmap;//Para convertir el drawable a bitmap
        RoundedBitmapDrawable redondo;//Para hacer la imagen redonda
        //Cogemos el drawable de la carpeta de recursos
        elemento = getResources().getDrawable(R.drawable.ic_1);
        //Convertimos el drawable a bitmap
        bitmap = ((BitmapDrawable) elemento).getBitmap();
        //Hacemos ese bitmap redondeado
        redondo = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
        //Redondeamos los bordes
        redondo.setCornerRadius(bitmap.getHeight());
        //Cogemos la referencia al imageView
        ImageView iv1 = (ImageView)findViewById(R.id.ivCad1);
        iv1.setImageDrawable(redondo);
        elemento = getResources().getDrawable(R.drawable.ic_2);
        bitmap = ((BitmapDrawable) elemento).getBitmap();
        redondo = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
        ImageView iv2 = (ImageView)findViewById(R.id.ivCad2);
        iv2.setImageDrawable(redondo);
        elemento = getResources().getDrawable(R.drawable.ic_3);
        bitmap = ((BitmapDrawable) elemento).getBitmap();
        redondo = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
        ImageView iv3 = (ImageView)findViewById(R.id.ivCad3);
        iv3.setImageDrawable(redondo);
        elemento = getResources().getDrawable(R.drawable.ic_4);
        bitmap = ((BitmapDrawable) elemento).getBitmap();
        redondo = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
        ImageView iv4 = (ImageView)findViewById(R.id.ivCad4);
        iv4.setImageDrawable(redondo);
        elemento = getResources().getDrawable(R.drawable.ic_5);
        bitmap = ((BitmapDrawable) elemento).getBitmap();
        redondo = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
        ImageView iv5 = (ImageView)findViewById(R.id.ivCad5);
        iv5.setImageDrawable(redondo);
        elemento = getResources().getDrawable(R.drawable.ic_6);
        bitmap = ((BitmapDrawable) elemento).getBitmap();
        redondo = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
        ImageView iv6 = (ImageView)findViewById(R.id.ivCad6);
        iv6.setImageDrawable(redondo);
        elemento = getResources().getDrawable(R.drawable.ic_7);
        bitmap = ((BitmapDrawable) elemento).getBitmap();
        redondo = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
        ImageView iv7 = (ImageView)findViewById(R.id.ivCad7);
        iv7.setImageDrawable(redondo);
        elemento = getResources().getDrawable(R.drawable.mas);
        bitmap = ((BitmapDrawable) elemento).getBitmap();
        redondo = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
        ImageView iv8 = (ImageView)findViewById(R.id.ivCadMas);
        iv8.setImageDrawable(redondo);
    }
}
