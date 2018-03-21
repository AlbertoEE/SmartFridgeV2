package net.ddns.smartfridge.smartfridgev2.vista.actividades.ca;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import static net.ddns.smartfridge.smartfridgev2.modelo.utiles.Permiso.PERM_FOTO2;

/**
 * Activity que representa la parte de insertar un alimento de manera manual, mediante un nombre y una foto
 */
public class InsertadorManualmenteActivity extends AppCompatActivity {
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
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
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

    /**
     * Método para llamar a la cámara y hacer una fotografíca del alimento
     *
     * @param view View sobre el que actúa el onClick
     */
    public void hacerFotoButton(View view){
        hacerFoto();
    }

    /**
     * Método para hacer una fotografría al alimento cuando lo queremos introducir de manera manual
     */
    private void hacerFoto(){
        permiso = new Permiso();
        Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri uri  = Uri.parse("file:///sdcard/photo.jpg");
        intentCamera.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, uri);
        intentCamera.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        if (permiso.permisoCamara2(this, this)
                && intentCamera.resolveActivity(this.getPackageManager()) != null){
               startActivityForResult(intentCamera, COD_CAMARA);
        }else{
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, permiso.PERM_FOTO2);
        }
    }

    /**
     * Método para mostrar la imagen hecha con la cámara al usuario
     */
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

    /**
     * Método para pasar al activity de confirmar alimento
     *
     * @param view View sobre el que actúa el onClick del botón
     */
    public void siguienteBoton(View view){
        String nombre = String.valueOf(etNombreAlimento.getText());
        Toast.makeText(this, nombre, Toast.LENGTH_SHORT).show();
        if(!nombre.equals("") ){
            Intent intent = new Intent(InsertadorManualmenteActivity.this, CaducidadAlimento.class);
            intent.putExtra("FotoBitMap", foto);
            intent.putExtra("ClasePadre", "InsertadorManualmenteActivity");
            intent.putExtra("NombreAlimento" , etNombreAlimento.getText());
            intent.putExtra("CODIGO_BARRAS", codigo_barras);
            Log.d("cod", "codigo 5: " + codigo_barras);
            startActivity(intent);
            //finish();
        }else{
            Toast.makeText(this, "Campo de nombre obligatorio", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * Método para mostrar al usuario las sugerencias de los nombres de los alimentos
     * @param cursor Cursor con todos los datos de los alimentos insertados manualmente con anterioridad
     * @return Lista con todos los Strings con los nombres
     */
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
        //super.onActivityResult(requestCode, resultCode, data);
        //Al volver de hacer la foto la colocamos en el lugar del botón de la cámara
        Log.d("LA PUERTA", "onActivityResult: " + requestCode + resultCode + data );
        if (requestCode == COD_CAMARA) {
            Log.d("jjjjj", "onActivityResult: " + "hoaalala");
            File file = new File(Environment.getExternalStorageDirectory().getPath(), "photo.jpg");
            Uri uri = Uri.fromFile(file);
            Bitmap bitmap;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                bitmap = crupAndScale(bitmap, 300);
                Log.d("MADRELAFOTO", "onActivityResult: " + bitmap);
                foto = bitmap;
                mostrarFoto();
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                Log.d("jjjjj", "onActivityResult: " + "hoaalala2");
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                Log.d("jjjjj", "onActivityResult: " + "hoaalala3");
            }
        }
    }

    /**
     * Método para escalar la imagen a la hora de guardarla
     * @param source Imagen que queremos escalar
     * @param scale Escala a aplicar
     * @return Imagen escalada
     */
    public static  Bitmap crupAndScale (Bitmap source,int scale){
        int factor = source.getHeight() <= source.getWidth() ? source.getHeight(): source.getWidth();
        int longer = source.getHeight() >= source.getWidth() ? source.getHeight(): source.getWidth();
        int x = source.getHeight() >= source.getWidth() ?0:(longer-factor)/2;
        int y = source.getHeight() <= source.getWidth() ?0:(longer-factor)/2;
        source = Bitmap.createBitmap(source, x, y, factor, factor);
        source = Bitmap.createScaledBitmap(source, scale, scale, false);
        return source;
    }

    /**
     * Gets foto.
     *
     * @return the foto
     */
    public static Bitmap getFoto() {
        return foto;
    }

    /**
     * Sets foto.
     *
     * @param foto the foto
     */
    public static void setFoto(Bitmap foto) {
        foto = foto;
    }

    //Cerramos la bbdd en el onDestroy

    @Override
    protected void onDestroy() {
        super.onDestroy();
        alimento_nuevoDB.cerrarConexion();
    }

    /**
     * Método para mostrar el tutorial al usuario
     */
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
                            //Cambiamos la variable en el sharedPreferences para que no se vuelva a mostrar el tutorial
                            SharedPreferences.Editor tutorialShowcasesEdit = tutorialShowcases.edit();
                            tutorialShowcasesEdit.putBoolean("runInsertarManualmente", false);
                            tutorialShowcasesEdit.apply();
                            s.hide();
                            break;
                    }
                }
            });
        }
    }
}
