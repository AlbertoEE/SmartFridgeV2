package net.ddns.smartfridge.smartfridgev2.vista;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import net.ddns.smartfridge.smartfridgev2.R;

public class InsertarManualmenteActivity extends AppCompatActivity {
    private TextView explicacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insertar_manualmente);
        explicacion = findViewById(R.id.tvExplicativo);

        explicacion.requestFocus();
    }
}
