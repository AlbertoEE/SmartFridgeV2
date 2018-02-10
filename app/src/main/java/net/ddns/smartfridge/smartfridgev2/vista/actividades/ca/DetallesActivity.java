package net.ddns.smartfridge.smartfridgev2.vista.actividades.ca;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aigestudio.wheelpicker.WheelPicker;

import net.ddns.smartfridge.smartfridgev2.R;
import net.ddns.smartfridge.smartfridgev2.modelo.adaptadores.CustomPageAdapter;
import net.ddns.smartfridge.smartfridgev2.modelo.basico.Alimento;
import net.ddns.smartfridge.smartfridgev2.modelo.basico.Alimento_Nuevo;
import net.ddns.smartfridge.smartfridgev2.modelo.servicios.ComprobarCaducidadIntentService;
import net.ddns.smartfridge.smartfridgev2.modelo.utiles.Dialogos;
import net.ddns.smartfridge.smartfridgev2.persistencia.gestores.AlimentoDB;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class DetallesActivity extends FragmentActivity {
    private ViewPager viewPager;
    private ArrayList<Alimento> alimentos;
    private AlimentoDB alimentoDB;
    private int posicion;
    private Cursor cursor;
    private ArrayList<Bitmap> imagenes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imagenes = new ArrayList<>();

        alimentoDB = new AlimentoDB(this);
        setContentView(R.layout.activity_detalles_viewpager);
        Log.d("SWIPE", "onCreate: estoy aqui en el detalles activity");
        viewPager = findViewById(R.id.viewpager);
        alimentos = (ArrayList<Alimento>) getIntent().getSerializableExtra("alimentosSinFoto");
        if (alimentos == null) {
            cursor = alimentoDB.getAlimentos();
            cargarArray();
        } else {
            imagenes = MiNeveraActivity.getImagenDetalles();
        }

        posicion = getIntent().getExtras().getInt("posicion");

        CustomPageAdapter pageAdapter = new CustomPageAdapter(getSupportFragmentManager(), alimentos, imagenes);
        viewPager.setAdapter(pageAdapter);

        viewPager.setCurrentItem(posicion);
    }

    public void cargarArray() {
        //Limpiamos el array
        alimentos = new ArrayList<>();
        //Comprobamos si hay datos en el cursor
        if (cursor.moveToFirst()) {
            //Variables necesarias para convertir el blob de la foto a bitmap
            byte[] byteArrayFoto;
            Bitmap bm = null;
            ByteArrayOutputStream stream = new ByteArrayOutputStream();

            for (int i = 0; i < cursor.getCount(); i++) { //Recorremos el cursor (no sé por qué puse un for)
                byteArrayFoto = cursor.getBlob(6);
                //no lo es por lo tanto comprimimos la foto y añadimos un nuevo alimento a nuestro array

                alimentos.add(new Alimento(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getInt(2),
                        cursor.getInt(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        null));
                if(byteArrayFoto != null){
                    bm = BitmapFactory.decodeByteArray(byteArrayFoto, 0, byteArrayFoto.length);
                    bm.compress(Bitmap.CompressFormat.PNG, 100, stream);
                }
                imagenes.add(bm);

                //siguiente registro del cursor
                cursor.moveToNext();
            }
        }
        //Cuando ya tenemos nuestro array entero, hacemos una copia de el porque nos va a hacer falta
        //para el método de filtrar
    }
    //Cerramos la bbdd en el onDestroy

    @Override
    protected void onDestroy() {
        super.onDestroy();
        alimentoDB.cerrarConexion();
    }
}
