package net.ddns.smartfridge.smartfridgev2.vista.actividades;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import net.ddns.smartfridge.smartfridgev2.R;
import net.ddns.smartfridge.smartfridgev2.modelo.utiles.Dialogos;

public class DialogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
        Dialogos d = new Dialogos(this, this);
        d.dialogListaCompra();
    }
}
