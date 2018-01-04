package net.ddns.smartfridge.smartfridgev2.vista;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;
import com.google.zxing.Result;
import me.dm7.barcodescanner.zxing.ZXingScannerView;


import net.ddns.smartfridge.smartfridgev2.R;

public class EscanerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler{

    private ZXingScannerView escanerView;
    public static final String TAG_CODIGO = "codigo";//Para hacer el paso al Intent
    public static final String TAG_TIPO_CODIGO = "tipoCodigo";//Para hacer el paso al Intent
    private Intent confirmar;//Intent que generamos para abrir un nuevo Activity
    public static final int DELAY_SCANNER = 2000;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        escanerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
        setContentView(escanerView);
    }

    @Override
    public void onResume() {
        super.onResume();
        escanerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        escanerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        escanerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void handleResult(Result resultado) {
        Toast.makeText(this, resultado.getText(), Toast.LENGTH_SHORT).show();
        // If you would like to resume scanning, call this method below:
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                escanerView.resumeCameraPreview(EscanerActivity.this);
            }
        }, DELAY_SCANNER);
        //Si reconoce un código de barras, abrimos el intent correspondiente para buscar correspondencia en la bbdd
        confirmar = new Intent(this, ConfirmarAlimentoActivity.class);
        //Pasamos el código de barras y el formato para comprobar que se encuentra en la bbdd
        confirmar.putExtra(TAG_CODIGO, resultado.getText());
        confirmar.putExtra(TAG_TIPO_CODIGO, resultado.getBarcodeFormat().toString());
        startActivity(confirmar);
        finish();
    }
}
