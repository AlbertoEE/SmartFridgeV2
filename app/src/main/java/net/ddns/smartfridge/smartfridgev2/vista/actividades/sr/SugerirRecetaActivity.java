package net.ddns.smartfridge.smartfridgev2.vista.actividades.sr;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import net.ddns.smartfridge.smartfridgev2.R;

public class SugerirRecetaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sugerir_receta);
        Intent intent = getIntent();
    }
}
