package net.ddns.smartfridge.smartfridgev2.vista.actividades.ca;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aigestudio.wheelpicker.WheelPicker;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

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

/**
 * The type Detalles activity.
 */
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
        //mostrarTutorial();
    }

    /**
     * Cargar array.
     */
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
        cursor.close();
        //Cuando ya tenemos nuestro array entero, hacemos una copia de el porque nos va a hacer falta
        //para el método de filtrar
    }
    //Cerramos la bbdd en el onDestroy

    @Override
    protected void onDestroy() {
        super.onDestroy();
        alimentoDB.cerrarConexion();
    }

    private void mostrarTutorial(){
        final SharedPreferences tutorialShowcases = getSharedPreferences("showcaseTutorial", MODE_PRIVATE);
        boolean run;
        run = tutorialShowcases.getBoolean("runDetalles", true);

        if(run){//Comprobamos si ya se ha mostrado el tutorial en algún momento

            //Creamos un nuevo LayoutParms para cambiar el botón de posición
            final RelativeLayout.LayoutParams lps = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lps.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            lps.addRule(RelativeLayout.CENTER_HORIZONTAL);
            // Ponemos márgenes al botón
            int margin = ((Number) (getResources().getDisplayMetrics().density * 16)).intValue();
            lps.setMargins(margin, margin, margin, margin);

            //Creamos el ShowCase
            final ShowcaseView s = new ShowcaseView.Builder(this)
                    .setTarget( new ViewTarget( ((View) findViewById(R.id.wheelUdsDetalles)) ) )
                    .setContentTitle("Modificar Unidades")
                    .setContentText("Modifica las uds disponibles girando la rueda. Si lo dejas a 0, el producto se eliminará.")
                    .hideOnTouchOutside()
                    .build();
            s.setButtonText("Siguiente");
            s.setButtonPosition(lps);
            //Comprobamos que el botón del showCase se pulsa para hacer el switch. Se va acomprobar el contador para ver si se muestra el siguiente showcas
            s.overrideButtonClick(new View.OnClickListener() {
                int contadorS = 0;

                @Override
                public void onClick(View v) {
                    contadorS++;
                    switch (contadorS) {
                        case 1:
                            /*Cambiamos la variable en el sharedPreferences para que no se vuelva a mostrar el tutorial
                            SharedPreferences.Editor tutorialShowcasesEdit = tutorialShowcases.edit();
                            tutorialShowcasesEdit.putBoolean("runDetalles", false);
                            tutorialShowcasesEdit.apply();*/
                            s.hide();
                            break;
                    }
                }
            });
        }
    }
}
