package net.ddns.smartfridge.smartfridgev2.vista;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonFactory;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionRequest;
import com.google.api.services.vision.v1.VisionRequestInitializer;

import net.ddns.smartfridge.smartfridgev2.R;
import net.ddns.smartfridge.smartfridgev2.modelo.CustomDialogProgressBar;
import net.ddns.smartfridge.smartfridgev2.modelo.Permiso;
import net.ddns.smartfridge.smartfridgev2.persistencia.GestorAlmacenamientoInterno;

import java.io.File;
import java.io.IOException;

public class IdentificarAlimentoActivity extends AppCompatActivity {
    public static final int PERMISOS = 5;//Cte que representa el valor que le daremos al parámetro onRequestPermissionsResult del grantResult, en el caso
    //de que el usuario no haya concedido los permisos necesarios
    private static final String KEY = "data";//Cte para el nombre de la clave "data"
    private Bitmap imagenCamara = null;//Para almacenar la imagen
    private static final String API_KEY= "AIzaSyDnjuzwlVTlgcubURXS3xhFwmuKBEvxGGQ";
    private GestorAlmacenamientoInterno gai;//Para almacenar la foto
    private static final String NOMBRE_FOTO_CAMARA = "imagenVision.png";//Nombre de la foto creada
    private Uri fotoUri;//Para almacenar la Uri de la foto para api Cloud Vision
    private static final int DIMENSION_BITMAP = 1000;//Para redimensionar el bitmap de la imagen
    private CustomDialogProgressBar customDialogProgressBar;//Para el progressbar personalizado
    private static final String HEADER = "X-Android-Package";//Para general el header de solicitudes http


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identificar_alimento);
        gai = new GestorAlmacenamientoInterno(this);
        customDialogProgressBar = new CustomDialogProgressBar(this);
    }

    public void escanear() {
        Intent intent = new Intent(this, EscanerActivity.class);
        //Para que no se guarde el histórico de códigos escaneados
        intent.putExtra("SAVE_HISTORY", false);
        startActivity(intent);
        overridePendingTransition(R.anim.right_in, R.anim.right_out);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Toast.makeText(this, "ResultCode: " + requestCode, Toast.LENGTH_SHORT).show();
        //Si viene del escaner
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String contents = data.getStringExtra("SCAN_RESULT");
                //Log.d("scaner", "contents: " + contents);
            } else if (resultCode == RESULT_CANCELED) {
                //Log.d("scaner", "RESULT_CANCELED");
            }
            //Si viene del api vision
        } else {
            if(resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                imagenCamara = (Bitmap) extras.get(KEY);
                //Comprobamos si se ha hecho una foto
                if (imagenCamara != null) {
                    //Guardamos la imagen
                    gai.guardarImagen(imagenCamara);
                    //Cogemos la uri de la foto que hacemos
                    fotoUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", cogerArchivoCamara());
                    //Llamamos al metodo cargarImagen y le pasamos la uri
                    cargarImagen(fotoUri);
                } else {//Si no se ha hecho, se lo indicamos al usuario
                    Toast.makeText(this, "Error al tomar la fotografía. Por favor, vuelva a intentarlo.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    //Llamaremos a este método para ver si están los permisos. Si están a true, llamaremos al método escanear()
    public void scaner(View v) {
        Permiso permiso = new Permiso();
        if (permiso.permisoCamara(this, this)) {
            escanear();
        } else {//Si no están los permisos, lo indicamos y no se podrá iniciar el scanner
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.CAMERA},
                    PERMISOS);
        }
    }

    //Para comprobar el resultado del permiso de acceso a la cámara
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            //Miramos el código de respuesta a qué permiso pertenece
            case PERMISOS: {
                if ((grantResults.length > 1) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    llamarHacerFoto();
                } else {
                    Toast.makeText(this, "Permiso concedido", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    //Llamaremos a este método para ver si están los permisos. Si están a true, llamaremos al método escanear()
    public void visionCloud(View v) {
        //Creamos la instancia del objeto Vision para usar Cloud Vision API.
        Vision.Builder visionBuilder = new Vision.Builder(new NetHttpTransport(), new AndroidJsonFactory(),null);
        visionBuilder.setVisionRequestInitializer(new VisionRequestInitializer(API_KEY));
        Vision vision = visionBuilder.build();
        //Solicitamos los permisos
        Permiso permiso = new Permiso();
        if (permiso.permisoCamara(this, this) && permiso.permisoEscritura(this, this) && (permiso.permisoLectura(this, this))) {
            llamarHacerFoto();
        } else {//Si no están los permisos, lo indicamos y no se podrá hacer la fotografía
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISOS);
        }
    }

    //Para llamar a la cámara para hacer la foto del Cloud
    public void llamarHacerFoto(){
        Intent iHacerFotografia = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //Miramos si hay alguna aplicación que pueda hacer la foto
        if (iHacerFotografia.resolveActivity(this.getPackageManager()) != null) {
            startActivityForResult(iHacerFotografia, Permiso.PERM_FOTO);
        }
    }

    public void insertarManualmenteButton(View view){
        Intent intent = new Intent(this, InsertarManualmenteActivity.class);
        startActivity(intent);
    }

    //Metodo para coger el fichero con la imagen hecha con la cámara
    public File cogerArchivoCamara(){
        File archivo = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return new File (archivo, NOMBRE_FOTO_CAMARA);
    }

    //Metodo para cargar la imagen
    public void cargarImagen(Uri uri){
        //Comprobamos que no esté vacía
        if (uri != null){
            //Primero escalamos la imagen
            try {
                Bitmap imagenEscalada = escalarImagen(MediaStore.Images.Media.getBitmap
                        (getContentResolver(), uri), DIMENSION_BITMAP);
                //LLAMADA AL ASYNC TASK PASANDO COMO PARÁMETRO ESTE BITMAP
                if (conexion()){

                } else {
                    Toast.makeText(this, "No hay conexión", Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                Log.d("redimensionar", "Error al ejecutar la consulta: " + e.getMessage());
                Toast.makeText(this, "Error al ejecutar la consulta. Por favor, vuelva a " +
                        "intentarlo.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Error al cargar la imagen. Vuelva a intentarlo", Toast.LENGTH_SHORT).show();
            Log.d("prueba", "Image picker da imagen a null");
        }
    }

    //Metodo para escalar la imagen y darle el tamaño de 1000x1000
    public Bitmap escalarImagen(Bitmap bitmap, int dimension){
        int anchoOriginal = bitmap.getWidth();
        int altoOriginal = bitmap.getHeight();
        int anchoRed = DIMENSION_BITMAP;
        int altoRed = DIMENSION_BITMAP;
        //Creamos los if para que se ajuste el tamaño
        if (altoOriginal > anchoOriginal){
            //Si el alto original es mayor que el ancho original
            altoRed = DIMENSION_BITMAP;
            anchoRed = (int) ((altoRed * (float) anchoOriginal) / (float) altoOriginal);
        } else if (anchoOriginal > altoOriginal){
            //Si el ancho original es mayor que el alto original
            anchoRed = DIMENSION_BITMAP;
            altoOriginal = (int) ((anchoRed * (float) altoOriginal) / (float) anchoOriginal);
        } else {
            //En el caso de que ambos valores sean iguales
            anchoRed = DIMENSION_BITMAP;
            altoRed = DIMENSION_BITMAP;
        }
        return Bitmap.createScaledBitmap(bitmap, anchoRed, altoRed, false);
    }
    //Comrpobamos si hay conexión
    public boolean conexion() {
        ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    //Creamos el AsyncTask para hacer la consulta a la web
    public class CloudVisionTask extends AsyncTask<Object,Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Mostramos el progressBar personalizado
            customDialogProgressBar.showDialogCuadrado();
        }

        @Override
        protected String doInBackground(Object... objects) {
            HttpTransport ht = AndroidHttp.newCompatibleTransport();
            //Para trabajar con el Json que nos devuelve la consulta
            com.google.api.client.json.JsonFactory jsf = GsonFactory.getDefaultInstance();
            //Metemos la clave y creamos el objeto que va a hacer las consultas
            //Le asignamos la clave del producto
            VisionRequestInitializer ri = new VisionRequestInitializer(API_KEY){
                //Hacemos la peticion para inicializar el servicio Cloud Api Vision
                @Override
                protected void initializeVisionRequest(VisionRequest<?> request) throws IOException {
                    super.initializeVisionRequest(request);
                    //Cogemos el nombre del package
                    String packageName = getPackageName();
                    //Para el header de HTTP usado para las solicitudes a las apis de Google
                    request.getRequestHeaders().set(HEADER, packageName);
                    //String signature = PackageManagerUtils.getSignature
                }
            };


            return null;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }
}
