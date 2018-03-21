package net.ddns.smartfridge.smartfridgev2.vista.actividades.ca;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionRequest;
import com.google.api.services.vision.v1.VisionRequestInitializer;
import com.google.api.services.vision.v1.model.AnnotateImageRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.Feature;
import com.google.api.services.vision.v1.model.Image;

import net.ddns.smartfridge.smartfridgev2.R;
import net.ddns.smartfridge.smartfridgev2.modelo.personalizaciones.CustomDialogProgressBar;
import net.ddns.smartfridge.smartfridgev2.modelo.servicios.CloudVision;
import net.ddns.smartfridge.smartfridgev2.modelo.utiles.Dialogos;
import net.ddns.smartfridge.smartfridgev2.modelo.utiles.Firma;
import net.ddns.smartfridge.smartfridgev2.modelo.utiles.Permiso;
import net.ddns.smartfridge.smartfridgev2.persistencia.gestores.AlimentoDB;
import net.ddns.smartfridge.smartfridgev2.persistencia.gestores.GestionAlmacenamientoInterno;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;

/**
 * Activity que representa las tres opciones de identificación de alimentos: escáner, Cloud Vision y manual
 */
public class IdentificadorAlimentoActivity extends AppCompatActivity {
    /**
     * Constante que representa el valor que le damos al parámetro onRequestPermissionsResult del grantResult, en el caso
     * de que el usuario no haya concedido los permisos necesarios
     */
    public static final int PERMISOS = 5;//Cte que representa el valor que le daremos al parámetro onRequestPermissionsResult del grantResult, en el caso
    //de que el usuario no haya concedido los permisos necesarios
    private static final String KEY = "data";//Cte para el nombre de la clave "data"
    private Bitmap imagenCamara = null;//Para almacenar la imagen
    private static final String API_KEY = "AIzaSyDnjuzwlVTlgcubURXS3xhFwmuKBEvxGGQ";
    private GestionAlmacenamientoInterno gai;//Para almacenar la foto
    private Uri fotoUri;//Para almacenar la Uri de la foto para api Cloud Vision
    private CustomDialogProgressBar customDialogProgressBar;//Para el progressbar personalizado
    private static final String HEADER = "X-Android-Package";//Para general el header de la solicitud http
    private static final String CERT = "X-Android-Cert";//Para el certificado en la validación
    private Bitmap imagenEscalada;//Para escalar el bitmap de la foto hecha por la cámara
    private static final int NUM_RESULTADOS = 10;//Para el número máximo de resultados que nos da el API Vision
    private static final String[] FEATURES = {"LABEL_DETECTION", "CROP_HINTS", "DOCUMENT_TEXT_DETECTION", "FACE_DETECTION", "IMAGE_PROPERTIES", "LANDMARK_DETECTION",
            "LOGO_DETECTION", "SAFE_SEARCH_DETECTION", "TEXT_DETECTION", "TYPE_UNSPECIFIED", "WEB_ANNOTATION"};//Parámetro a detectar en la imagen
    private static Feature labelDetection;//Para darle las características del label_detection
    private Intent i;//Para recoger el intent de otra activity
    private Dialogos dialogos;//Para utilizar la clase con los diálogos
    private String codigo_barras;//Para recoger el código de barras cuando venga de un código no encontrado
    private CloudVision cvision;//Para crear una instancia y usar los métodos
    private static final int VALOR=16;//Valor para redimensionar la imagen

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identificar_alimento);
        AlimentoDB alimentoDB = new AlimentoDB(this);
        dialogos = new Dialogos(this, this);
        gai = new GestionAlmacenamientoInterno(this);
        customDialogProgressBar = new CustomDialogProgressBar(this);
        cvision = new CloudVision();
        try {
            i = getIntent();
            if (i.getStringExtra(getString(R.string.clasePadre)).equals(getString(R.string.confirmador))) {
                dialogos.dialogNoCodBarras();
                codigo_barras = i.getStringExtra(getString(R.string.cod_barras));
                Log.d("cod", "codigo 2: " + codigo_barras);
            }
        } catch (NullPointerException e){
            //No hacemos nada
        }
        mostrarTutorial();
    }

    /**
     * Método que abre el escáner para detectar el código de barras
     */
    public void escanear() {
        Intent intent = new Intent(this, EscanerActivity.class);
        //Para que no se guarde el histórico de códigos escaneados
        intent.putExtra(getString(R.string.save_hist), false);
        startActivity(intent);
        overridePendingTransition(R.anim.right_in, R.anim.right_out);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Toast.makeText(this, "ResultCode: " + requestCode, Toast.LENGTH_SHORT).show();
        //Si viene del escaner
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String contents = data.getStringExtra(getString(R.string.escan_r));
                //Log.d("scaner", "contents: " + contents);
            } else if (resultCode == RESULT_CANCELED) {
                //Log.d("scaner", "RESULT_CANCELED");
            }
            //Si viene del api vision
        } else {
            if (resultCode == RESULT_OK) {
//                Bundle extras = data.getExtras();
             //   imagenCamara = (Bitmap) extras.get(KEY);

                File file = new File(Environment.getExternalStorageDirectory().getPath(), "photo.jpg");
                Uri uri = Uri.fromFile(file);
                Bitmap bitmap;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    bitmap = crupAndScale(bitmap, 300);
                    imagenCamara = bitmap;
                    fotoUri = Uri.parse(gai.guardarImagen(bitmap));
                    //Llamamos al metodo cargarImagen y le pasamos la uri
                    cargarImagen(fotoUri);
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    Log.d("jjjjj", "onActivityResult: " + "hoaalala2");
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    Log.d("jjjjj", "onActivityResult: " + "hoaalala3");
                }

                //Comprobamos si se ha hecho una foto
                /*if (imagenCamara != null) {
                    //Guardamos la imagen
                    gai.guardarImagen(imagenCamara);
                    //Cogemos la uri de la foto que hacemos
                    //Log.d("seguimiento", "uri: " + FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", cogerArchivoCamara()));
                    //fotoUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", cogerArchivoCamara());
                    //File f = cogerArchivoCamara();

                } else {//Si no se ha hecho, se lo indicamos al usuario
                    Toast.makeText(this, "Error al tomar la fotografía. Por favor, vuelva a intentarlo.", Toast.LENGTH_LONG).show();
                }*/
            }
        }
    }

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
     * Método para ver si están los permisos. Si están a true, llamaremos al método escanear().
     *
     * @param v View sobre el que actúa el onClick
     */
    public void scaner(View v) {
        Permiso permiso = new Permiso();
        if (permiso.permisoCamara(this, this)) {
            escanear();
        } else {//Si no están los permisos, lo indicamos y no se podrá iniciar el scanner
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.CAMERA},
                    320);
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
                    //Toast.makeText(this, "Permiso concedido", Toast.LENGTH_SHORT).show();
                } else {
                    //Toast.makeText(this, "Permiso denegado", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case 320: {
                if ((grantResults.length > 1) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    escanear();
                    //Toast.makeText(this, "Permiso concedido", Toast.LENGTH_SHORT).show();
                } else {
                    //Toast.makeText(this, "Permiso denegado", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

    /**
     * Método para ver si están los permisos. Si están a true, llamaremos al método llamarHacerFoto()
     *
     * @param v View sobre el que actúa el onClick
     */
    public void visionCloud(View v) {
        //Creamos la instancia del objeto Vision para usar Cloud Vision API.

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

    /**
     * Método para llamar a la cámara para hacer la foto del Cloud
     */
    public void llamarHacerFoto() {
        Intent iHacerFotografia = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri uri  = Uri.parse("file:///sdcard/photo.jpg");
        iHacerFotografia.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, uri);
        iHacerFotografia.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        //Miramos si hay alguna aplicación que pueda hacer la foto
        if (iHacerFotografia.resolveActivity(this.getPackageManager()) != null) {
            startActivityForResult(iHacerFotografia, Permiso.PERM_FOTO);
        }
    }

    /**
     * Método para abrir la opción de la inserción manual de alimentos
     *
     * @param view View sobre el que actúa el onClick
     */
    public void insertarManualmenteButton(View view) {
        Log.d("cod", "codigo 3_A: " + codigo_barras);
        Intent intent = new Intent(this, InsertadorManualmenteActivity.class);
        intent.putExtra(getString(R.string.cod_barras), codigo_barras);
        Log.d("cod", "codigo 3: " + codigo_barras);
        startActivity(intent);
    }

    /**
     * Metodo para coger el fichero con la imagen hecha con la cámara
     *
     * @return fichero con la imagen hecha con la cámara
     */
    public File cogerArchivoCamara() {
        File archivo = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String directorioAlmcto;//Para darle el nombre a la imagen
        File fichero;
        GestionAlmacenamientoInterno gi = new GestionAlmacenamientoInterno(this);
        directorioAlmcto = gi.cogerDirectorio();
        fichero = new File(directorioAlmcto + "/imagenVision.png");
        return fichero;
    }

    /**
     * Metodo para cargar la imagen y ejecutar el AsyncTask
     *
     * @param uri Uri con la imagen almacenada que queremos comprobar
     */
    public void cargarImagen(Uri uri) {
        //Comprobamos que no esté vacía
        if (uri != null) {
            //Primero escalamos la imagen
            try {
                //Ojo que puede ar fallo y sea esclarImagen(imagenCamara)
                imagenEscalada = cvision.escalarImagen(MediaStore.Images.Media.getBitmap
                        (getContentResolver(), uri), CloudVision.DIMENSION_BITMAP);
                //LLAMADA AL ASYNC TASK PASANDO COMO PARÁMETRO ESTE BITMAP
                if (conexion()) {
                    new CloudVisionTask().execute(imagenEscalada);
                } else {
                    Toast.makeText(this, "No hay conexión", Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                Log.d("seguimiento", "Error al ejecutar la consulta: " + e.getMessage());
                Toast.makeText(this, getString(R.string.Error_1), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, getString(R.string.Error_2), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Método para comprobar si hay conexión a internet
     *
     * @return true si hay conexión y false si no la hay
     */
    public boolean conexion() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    /**
     * Clase para hacer el AsyncTask del CloudVision
     */
    public class CloudVisionTask extends AsyncTask<Object, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Mostramos el progressBar personalizado
            customDialogProgressBar.showDialogCuadrado();
        }

        @Override
        protected String doInBackground(Object... objects) {
            try {
                //Construimos la instancia de Vision API
                HttpTransport ht = AndroidHttp.newCompatibleTransport();
                //Para trabajar con el Json que nos devuelve la consulta
                JsonFactory jsf = GsonFactory.getDefaultInstance();
                //Metemos la clave y creamos el objeto que va a hacer las consultas, pasándole lo necesario para inicializarla
                //Le asignamos la clave del producto
                VisionRequestInitializer ri = new VisionRequestInitializer(API_KEY) {
                    //Hacemos la peticion para inicializar el servicio Cloud Api Vision
                    @Override
                    protected void initializeVisionRequest(VisionRequest<?> request) throws IOException {
                        super.initializeVisionRequest(request);
                        //Cogemos el nombre del package
                        String nombrePackage = getPackageName();
                        //Para el header de HTTP usado para las solicitudes a las apis de Google
                        request.getRequestHeaders().set(HEADER, nombrePackage);
                        //Recogemos en un string la firma del proyecto
                        String firma = Firma.getFirma(getPackageManager(), nombrePackage);
                        //Le pasamos a los headers la cte con el valor del certificado para la app  y la firma que hemos obtenido antes
                        request.getRequestHeaders().set(CERT, firma);
                    }
                };
            /*Una vez que ya tenemos inicializado el objeto sobre el que haremos las consultas, creamos el Vision.Builder
            para trabajar con el json que nos devuelve la consulta*/
                Vision.Builder builder = new Vision.Builder(ht, jsf, null);
                //Le pasamos como argumento el objeto VisionRequestInitializer inicializado al builder
                builder.setVisionRequestInitializer(ri);
            /*Creamos el objeto Vision con el builder. Este objeto se va a encargar de manejar internamente el transporte HTTP
            y la lógica de análisis JSON para cada solicitud y cada respuesta que hagamos*/
                Vision vision = builder.build();
                //Creamos la solicitud con la imagen escalada
                AnnotateImageRequest air = new AnnotateImageRequest();
                //Escalamos la imagen
                Image imagen = cvision.convertirBitmap(imagenEscalada);
                //Le asignamos la imagen escalada al AnnotateImageRequest
                air.setImage(imagen);
                // Añadimos las características de la petición
                labelDetection = new Feature();
                //Añadimos qué queremos identificar en la fotografía
                labelDetection.setType(FEATURES[0]);
                //Añadimos el número máximo de resultados que queremos que nos devuelva la respuesta
                labelDetection.setMaxResults(NUM_RESULTADOS);
                air.setFeatures(Collections.singletonList(labelDetection));

                //Ejecutamos la petición al servicio.
                BatchAnnotateImagesRequest bair = new BatchAnnotateImagesRequest();
                //Le pasamos el AnnotateImageRequest con las caraceterísticas que le hemos dado anteriormente
                bair.setRequests(Collections.singletonList(air));
            /*Ahora tratamos la respuesta que nos envíe el API Vision.Si el tamaño de la imagen es muy grande, puede darnos error
            cuando intentemos comprimirlo a GZIP, lo inhabilitamos. Almacenamos la respuesta en un BatchAnnotateImagesResponse*/
                BatchAnnotateImagesResponse response = vision.images().annotate(bair).setDisableGZipContent(true).execute();
                Log.d("seguimiento", "creada Cloud Vision request objetc, enviando consulta");
                //El metodo de tratarRespuesta nos devuelve un String
                return cvision.tratarRespuesta(response);
            } catch (IOException e) {
                Log.d("seguimiento", "Error al ejecutar API Vision: " + e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            customDialogProgressBar.endDialog();
            Log.d("json", "mensaje: " + s);
            //Vamos a crear el Intent para abrir el activity de ConfirmadorAlimentoActivity
            Intent i = new Intent(getApplicationContext(), ConfirmadorAlimentoActivity.class);
            //Le pasamos el nombre de la clase padre
            i.putExtra(getString(R.string.clasePadre), getString(R.string.identificador));
            if (s!=null){
                //Le pasamos el nombre del elemento
                i.putExtra(getString(R.string.nom_Cl), s);
                //Si hemos detectado algún nombre, cambiaremos la variable a true
                i.putExtra(getString(R.string.ele_encontrado), true);
            } else {
                //Controlamos que haya algún dato
                i.putExtra(getString(R.string.nom_Cl), getString(R.string.ele_des));
                //Si no hemos detectado el nombre, cambiaremos la variable a false
                i.putExtra(getString(R.string.ele_encontrado), false);
            }
            //Le pasamos la imagen
            i.putExtra(getString(R.string.img_Cl), imagenCamara);
            startActivity(i);
        }
    }

    /**
     * Método que muestra el tutorial al usuario
     */
    private void mostrarTutorial() {
        final SharedPreferences tutorialShowcases = getSharedPreferences(getString(R.string.tutorialSP), MODE_PRIVATE);
        boolean run;
        run = tutorialShowcases.getBoolean(getString(R.string.tutorial3), true);
        if (run) {//Comprobamos si ya se ha mostrado el tutorial en algún momento
            //Creamos el ShowCase
            final ShowcaseView s = new ShowcaseView.Builder(this)
                    .setTarget(new ViewTarget(((View) findViewById(R.id.ibEscaner))))
                    .setContentTitle(getString(R.string.sc))
                    .setContentText(getString(R.string.sc_texto))
                    .hideOnTouchOutside()
                    .build();
            s.setButtonText(getString(R.string.siguiente));
            //Creamos un nuevo LayoutParms para cambiar el botón de posición
            final RelativeLayout.LayoutParams lps = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lps.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            lps.addRule(RelativeLayout.CENTER_HORIZONTAL);
            // Ponemos márgenes al botón
            int margin = ((Number) (getResources().getDisplayMetrics().density * VALOR)).intValue();
            lps.setMargins(margin, margin, margin, margin);
            s.setButtonPosition(lps);
            //Comprobamos que el botón del showCase se pulsa para hacer el switch. Se va acomprobar el contador para ver si se muestra el siguiente showcas
            s.overrideButtonClick(new View.OnClickListener() {
                int contadorS = 0;

                @Override
                public void onClick(View v) {
                    contadorS++;
                    switch (contadorS) {
                        case 1:
                            // Ponemos márgenes al botón
                            s.setTarget(new ViewTarget(((View) findViewById(R.id.ibCloudVision))));
                            s.setContentTitle(getString(R.string.identificar));
                            s.setContentText(getString(R.string.identificar_t));
                            break;
                        case 2:
                            s.setTarget(new ViewTarget(((View) findViewById(R.id.ibManual))));
                            s.setContentTitle(getString(R.string.identificar_manual));
                            s.setContentText(getString(R.string.identificar_manual_t));
                            break;

                        case 3:
                            //Cambiamos la variable en el sharedPreferences para que no se vuelva a mostrar el tutorial
                            SharedPreferences.Editor tutorialShowcasesEdit = tutorialShowcases.edit();
                            tutorialShowcasesEdit.putBoolean(getString(R.string.tutorial3), false);
                            tutorialShowcasesEdit.apply();
                            s.hide();
                            break;
                    }
                }
            });
        }
    }
}
