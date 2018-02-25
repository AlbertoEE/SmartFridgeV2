package net.ddns.smartfridge.smartfridgev2.vista.actividades.ca;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

import net.ddns.smartfridge.smartfridgev2.R;
import net.ddns.smartfridge.smartfridgev2.modelo.utiles.Permiso;
import net.ddns.smartfridge.smartfridgev2.persistencia.gestores.Alimento_NuevoDB;

import java.io.ByteArrayOutputStream;

import static net.ddns.smartfridge.smartfridgev2.modelo.utiles.Permiso.PERM_FOTO2;

public class InsertarManualmenteActivity extends AppCompatActivity {
    private TextView explicacion;
    private Permiso permiso;
    private static Bitmap foto;
    private AutoCompleteTextView etNombreAlimento;
    private final static int COD_CAMARA = 1;
    private Alimento_NuevoDB alimento_nuevoDB;
    private Intent intent;//Para recoger el intent lanzado desde otro activity
    private String codigo_barras;//Para recoger el código de barras cuando venga de un código no encontrado



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        foto = null;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insertar_manualmente);
        alimento_nuevoDB = new Alimento_NuevoDB(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, generarSugerencias(alimento_nuevoDB.getAlimentosNuevos()));
        cargarMarquee();
        etNombreAlimento = (AutoCompleteTextView) findViewById(R.id.etNombreAlimento);
        etNombreAlimento.setAdapter(adapter);
        try{
            intent = getIntent();
            codigo_barras = intent.getStringExtra("CODIGO_BARRAS");
            Log.d("cod", "codigo 4: " + codigo_barras);
        } catch (NullPointerException e){
            //No hacemos nada
        }
        mostrarTutorial();
    }

    private void cargarMarquee(){
        explicacion = (TextView) findViewById(R.id.tvExplicativo);
        explicacion.requestFocus();
        explicacion.setSelected(true);
    }

    public void hacerFotoButton(View view){
        hacerFoto();
    }

    private void hacerFoto(){
        permiso = new Permiso();
        Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Log.d("PERMISOCAMARA2", String.valueOf(permiso.permisoCamara2(this, this)));
        if (permiso.permisoCamara2(this, this)
                && intentCamera.resolveActivity(this.getPackageManager()) != null){
               startActivityForResult(intentCamera, COD_CAMARA);
        }else{
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, permiso.PERM_FOTO2);
        }
    }

    private void mostrarFoto(){
        ImageButton ibCamara = (ImageButton) findViewById(R.id.ibCamara);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        this.foto.compress(Bitmap.CompressFormat.PNG, 100, stream);
        try {
            Glide.with(this)
                    .load(stream.toByteArray())
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(ibCamara);
        } catch (Exception e){
            Toast.makeText(this, "Imagen no disponible", Toast.LENGTH_SHORT).show();
        }
    }

    public void siguienteBoton(View view){
        String nombre = String.valueOf(etNombreAlimento.getText());
        Toast.makeText(this, nombre, Toast.LENGTH_SHORT).show();
        if(!nombre.equals("") ){
            Intent intent = new Intent(InsertarManualmenteActivity.this, CaducidadAlimento.class);
            intent.putExtra("FotoBitMap", foto);
            intent.putExtra("ClasePadre", "InsertarManualmenteActivity");
            intent.putExtra("NombreAlimento" , etNombreAlimento.getText());
            intent.putExtra("CODIGO_BARRAS", codigo_barras);
            Log.d("cod", "codigo 5: " + codigo_barras);
            startActivity(intent);
            //finish();
        }else{
            Toast.makeText(this, "Campo de nombre obligatorio", Toast.LENGTH_SHORT).show();
        }

    }
    
    private String[] generarSugerencias(Cursor cursor){
        int count = cursor.getCount();
        int contador = 0;
        Log.d("Count", "generarSugerencias: " + count);
        String[] alimentos = new String[count];
        //Log.d("String", "NOMBRE: " + cursor.getString(0));
        //Log.d("String", "NOMBRE: " + cursor.getString(1));
        if(cursor.moveToFirst()){
            do{
                alimentos[contador] = cursor.getString(0);
                contador++;
            }while(cursor.moveToNext() && contador != count);
        }
        cursor.close();
        return alimentos;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch(requestCode){
            case PERM_FOTO2: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    hacerFoto();
                } else{
                    Toast.makeText(this, "No se ha podido abrir la cámara", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Al volver de hacer la foto la colocamos en el lugar del botón de la cámara
        if (requestCode == COD_CAMARA && resultCode != RESULT_CANCELED && data != null) {
            //Bundle extras = data.getExtras();
            foto = (Bitmap) data.getExtras().get("data");
            if(foto != null){
                mostrarFoto();
            } else{
                Toast.makeText(this, "Error al tomar la foto", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static Bitmap getFoto() {
        return foto;
    }

    public static void setFoto(Bitmap foto) {
        foto = foto;
    }

    //Cerramos la bbdd en el onDestroy

    @Override
    protected void onDestroy() {
        super.onDestroy();
        alimento_nuevoDB.cerrarConexion();
    }

    private void mostrarTutorial(){
        final SharedPreferences tutorialShowcases = getSharedPreferences("showcaseTutorial", MODE_PRIVATE);
        boolean run;
        run = tutorialShowcases.getBoolean("runInsertarManualmente", true);

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
                    .setTarget( new ViewTarget( ((View) findViewById(R.id.etNombreAlimento)) ) )
                    .setContentTitle("Nombre del alimento")
                    .setContentText("Escribe el nombre del alimento que quieras incluir. Se te mostrará la lista con las sugerencias" +
                            " de productos que hayas incluido manualmente con anterioridad")
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

                            s.setTarget( new ViewTarget( ((View) findViewById(R.id.ibCamara)) ) );
                            s.setContentTitle("Realizar fotografía");
                            s.setContentText("Pulsa para realizar una fotografía del alimento.");
                            break;

                        case 2:
                            s.setTarget( new ViewTarget( ((View) findViewById(R.id.ibSiguiente)) )  );
                            s.setContentTitle("Aceptar");
                            s.setContentText("Pulsa para confirmar los datos y pasar a la pantalla de insertar caducidad y cantidad.");
                            break;

                        case 3:
                            /*Cambiamos la variable en el sharedPreferences para que no se vuelva a mostrar el tutorial
                            SharedPreferences.Editor tutorialShowcasesEdit = tutorialShowcases.edit();
                            tutorialShowcasesEdit.putBoolean("runInsertarManualmente", false);
                            tutorialShowcasesEdit.apply();*/
                            s.hide();
                            break;
                    }
                }
            });
        }
    }
}
