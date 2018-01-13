package net.ddns.smartfridge.smartfridgev2.vista;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
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
import com.shashank.sony.fancygifdialoglib.FancyGifDialog;
import com.shashank.sony.fancygifdialoglib.FancyGifDialogListener;

import net.ddns.smartfridge.smartfridgev2.R;
import net.ddns.smartfridge.smartfridgev2.modelo.Alimento_Codigo;
import net.ddns.smartfridge.smartfridgev2.modelo.Dialogos;
import net.ddns.smartfridge.smartfridgev2.modelo.escuchadores.CustomOnDragListener;
import net.ddns.smartfridge.smartfridgev2.modelo.escuchadores.CustomOnDragListener2;
import net.ddns.smartfridge.smartfridgev2.modelo.escuchadores.CustomOnLongClickListener;

import java.util.ArrayList;
import java.util.List;

public class CaducidadAlimento extends AppCompatActivity {
    public static final int MAXUDS = 50;//Número máximo de uds del WheelPicker
    private int unidadesWheel;//Unidades del Wheel Picker
    private Alimento_Codigo ac;//Para almacenar el objeto que recojamos el ConfirmarAlimentoActivity
    private int tiempo_Caducidad;//Para almacenar los días de caducidad
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = getIntent();
        ac = ConfirmarAlimentoActivity.getAlimento();
        setContentView(R.layout.activity_caducidad_alimento);
        //Cogemos el drawable de la carpeta de recursos
        Drawable elemento = getResources().getDrawable(R.drawable.uno);
        //Convertimos el drawable a bitmap
        Bitmap bitmap = ((BitmapDrawable) elemento).getBitmap();
        //Hacemos ese bitmap redondeado
        RoundedBitmapDrawable redondo = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
        //Redondeamos los bordes
        redondo.setCornerRadius(bitmap.getHeight());
        //Cogemos la referencia al imageView
        ImageView iv1 = (ImageView)findViewById(R.id.ivCad1);
        iv1.setImageDrawable(redondo);
        ImageView iv2 = (ImageView)findViewById(R.id.ivCad2);
        iv2.setImageDrawable(redondo);
        /*ImageView iv3 = (ImageView)findViewById(R.id.ivCad3);
        iv3.setImageDrawable(redondo);*/
        ImageView iv4 = (ImageView)findViewById(R.id.ivCad4);
        iv4.setImageDrawable(redondo);
        ImageView iv5 = (ImageView)findViewById(R.id.ivCad5);
        iv5.setImageDrawable(redondo);
        /*ImageView iv6 = (ImageView)findViewById(R.id.ivCad6);
        iv6.setImageDrawable(redondo);*/
        ImageView iv7 = (ImageView)findViewById(R.id.ivCad7);
        iv7.setImageDrawable(redondo);
        ImageView iv8 = (ImageView)findViewById(R.id.ivCadMas);
        iv8.setImageDrawable(this.getResources().getDrawable(R.drawable.uno));
        ImageView dd = (ImageView)findViewById(R.id.ivDropZone);
        dd.setImageBitmap(ac.getImagen());
        cargarDragAndDrop();

        WheelPicker wheelPicker = (WheelPicker) findViewById(R.id.wheelUds);
        wheel(wheelPicker);
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
        //
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
                Log.d("uds", "uds: " + unidadesWheel);
                //Log.d("XEXU", String.valueOf(tiempo_Caducidad));
            }
        });
    }

    //Metodo que mostrará un dialog con la caducidad y las uds seleccionads
    public void confirmarCaducidad(View v){
        /*
        Dialogos dialogos = new Dialogos(this,this);
        dialogos.dialogCaducidad(unidadesWheel, tiempo_Caducidad);*/
        new FancyGifDialog.Builder(this)
                .setTitle("Mira que dialog más chulo Alberto!!")
                .setMessage("Te gusta este dialog para usarlo en el Smart Fridge ¡¡¡Y lo sabes!!!!")
                .setNegativeBtnText("Cancel")
                .setPositiveBtnBackground("#FF4081")
                .setPositiveBtnText("Dale anda")
                .setNegativeBtnBackground("#FFA9A7A8")
                .setGifResource(R.drawable.gif1)   //Pass your Gif here
                .isCancellable(true)
                .OnPositiveClicked(new FancyGifDialogListener() {
                    @Override
                    public void OnClick() {
                        Toast.makeText(getApplicationContext(), "Elemento guardado correctamente en Tu Nevera", Toast.LENGTH_SHORT).show();
                    }
                })
                .OnNegativeClicked(new FancyGifDialogListener() {
                    @Override
                    public void OnClick() {
                        //Programar
                    }
                })
                .build();
    }
}
